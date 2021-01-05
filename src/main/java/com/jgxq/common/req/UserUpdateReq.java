package com.jgxq.common.req;

import lombok.Data;

/**
 * @author LuCong
 * @since 2021-01-05
 **/
@Data
public class UserUpdateReq {

    private String headImage;

    /**
     * 城市
     */
    private String city;

    /**
     * 个人简介
     */
    private String description;
    /**
     * 昵称
     */
    private String nickName;
}
