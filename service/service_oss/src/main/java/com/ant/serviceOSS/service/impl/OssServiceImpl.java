package com.ant.serviceOSS.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.ant.serviceOSS.config.properties.AliyunOSSProperties;
import com.ant.serviceOSS.service.OssService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@EnableConfigurationProperties(AliyunOSSProperties.class)
public class OssServiceImpl implements OssService {

    private final AliyunOSSProperties ossProperties;

    public OssServiceImpl(AliyunOSSProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    @Override
    public String uploadAvatar(MultipartFile file, String host) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ossProperties.getBucketname();


        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        String url = null;
        try {
            // 上传文件名
            String objectName = file.getOriginalFilename();
            // 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            objectName = uuid+objectName;
            // 如果没有指定host，则默认把文件按照日期进行分离
            if(StringUtils.isBlank(host)) {
                //  把文件按照日期进行分类
                String date = new DateTime().toString("yyyy/MM/dd");
                objectName = date + "/" + objectName;
            } else {
                // 否则放到指定的目录下
                objectName = host + "/" +objectName;
            }
            // 文件流
            InputStream inputStream = file.getInputStream();
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
            // 拼接图片url返回
            url = "https://"+bucketName+"."+endpoint+"/"+objectName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }
}
