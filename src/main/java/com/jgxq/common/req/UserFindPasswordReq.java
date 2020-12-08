package com.jgxq.common.req;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@Data
public class UserFindPasswordReq {

    @Email(message = "邮箱地址不合法")
    String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 15, message = "密码长度只能在6-15之间")
    String password;

    @NotBlank(message = "验证码不能为空")
    String verificationCode;

}
