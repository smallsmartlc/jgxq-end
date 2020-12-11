package com.jgxq.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionReq {
    private String time;
    private List<ActionInfoReq> infoList;
}
