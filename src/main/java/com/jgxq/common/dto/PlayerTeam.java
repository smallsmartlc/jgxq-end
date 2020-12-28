package com.jgxq.common.dto;

import lombok.Data;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
public class PlayerTeam {
    private Integer id;
    private String name;
    private String headImage;
    private String nation;
    private Integer number;
    private Integer position;
    private Integer age;
}
