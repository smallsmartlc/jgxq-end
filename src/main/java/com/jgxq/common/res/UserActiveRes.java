package com.jgxq.common.res;

import lombok.Data;

/**
 * @author LuCong
 * @since 2020-12-15
 **/
@Data
public class UserActiveRes {

    private UserRes userInfo;

    private Integer talks;

    private Integer comments;

    private Integer focus;

    private Integer fans;

    private boolean focused;

}
