package com.jgxq.common.req;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
@Data
public class UserEmailLoginReq {

    @Email(message = "邮箱地址不合法")
    String email;

    @NotBlank(message = "验证码不能为空")
    String verificationCode;
}
