package com.ant.serviceAcl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ant.serviceAcl.entity.Permission;
import com.ant.serviceAcl.entity.RolePermission;
import com.ant.serviceAcl.entity.User;
import com.ant.serviceAcl.helper.MemuHelper;
import com.ant.serviceAcl.helper.PermissionHelper;
import com.ant.serviceAcl.mapper.PermissionMapper;
import com.ant.serviceAcl.service.PermissionService;
import com.ant.serviceAcl.service.RolePermissionService;
import com.ant.serviceAcl.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;
    
    // 获取全部菜单,递归查询
    @Override
    public List<Permission> queryAllMenu() {

        // 1、查询菜单表所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        // 对菜单数据进行封装
        List<Permission> result = bulidPermission(permissionList);

        return result;
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }


        List<Permission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {
        // 删除角色之前拥有的权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id",roleId);
        rolePermissionService.remove(queryWrapper);

        // 根据roleId和权限id 构建插入数据库中的对象，并调用mybatis-plus生成的方法将数据插入到数据库中
        List<RolePermission> rolePermissions = new ArrayList<>();
        Arrays.asList(permissionIds).stream().forEach(permissionId -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissions.add(rolePermission);
        });
        rolePermissionService.saveBatch(rolePermissions);

    }

    // 递归删除菜单
    // 查出所有应该删除的菜单id，使用mybatis-plus自动生成的方法进行删除
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();

        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        List<Permission> permissionList = baseMapper.selectList(queryWrapper);

        selectChildListById(id, permissionList, idList);

        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     *	递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(String id, List<Permission> permissionList, List<String> idList) {

        permissionList.stream().forEach(permission -> {
            if(id.equals(permission.getPid())){
                idList.add(permission.getId());
                selectChildListById(permission.getId(),permissionList,idList);
            }
        });
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> bulid(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }


    //========================递归查询所有菜单================================================

    // 把返回所有菜单list集合进行封装的方法
    public static List<Permission> bulidPermission(List<Permission> permissionList) {
        // 创建最后封装结果的list
        List<Permission> finalList = new ArrayList<>();

        // 找到顶级菜单，从顶级菜单开始递归封装菜单数据，顶级菜单为pid = 0 的菜单
        for(Permission permission:permissionList) {
            if("0".equals(permission.getPid())){
                permission.setLevel(1);
                // 递归查询顶级菜单的下一级菜单
                finalList.add(selectChildren(permission,permissionList));
            }
        }

        return finalList;
    }

    // 递归查询函数
    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        // 将当前菜单的子菜单列表初始化，防止出现NPE
        permissionNode.setChildren(new ArrayList<Permission>());

        // 遍历所有菜单列表，查询当前菜单的子菜单，并从子菜单继续向下递归查询
        for(Permission permission:permissionList){
            if(permissionNode.getId().equals(permission.getPid())){
                permission.setLevel(permissionNode.getLevel()+1);
                permissionNode.getChildren().add(selectChildren(permission,permissionList));
            }
        }

        return permissionNode;
    }


    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<Permission>  wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(),idList);
        });
    }
}
