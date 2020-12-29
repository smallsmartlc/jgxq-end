package com.jgxq.common.res;

import lombok.Data;

import java.util.Date;

/**
 * @author LuCong
 * @since 2020-12-29
 **/
@Data
public class CollectNewsRes {
    private Integer id;
    private Date createTime;
    private NewsBasicRes news;
}
