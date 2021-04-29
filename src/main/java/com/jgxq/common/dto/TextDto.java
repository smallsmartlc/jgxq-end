package com.jgxq.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author LuCong
 * @since 2021-04-29
 **/
@Data
public class TextDto {

    @NotBlank(message = "内容不能为空")
    private String text;
}
