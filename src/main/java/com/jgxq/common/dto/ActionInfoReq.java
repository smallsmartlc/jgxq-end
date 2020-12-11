package com.jgxq.common.dto;

import lombok.Data;


/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
public class ActionInfoReq {
    //是否为主队事件
    private Boolean home;
    //事件类型
    private Integer type;
    //球员id
    private Integer playerId;

}
