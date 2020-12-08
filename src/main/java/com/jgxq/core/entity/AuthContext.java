package com.jgxq.core.entity;

import lombok.Data;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@Data
public class AuthContext {
    private String userkey;
    private String nickName;
    private String password;
    private String email;
    private String headImage;
    private String city;
    private String vip;
    private Integer homeTeam;


}
