package com.ant.commonutils.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberBaseInfo {
    private String id;

    private String nickname;

    private String mobile;

    private String avatar;
}
