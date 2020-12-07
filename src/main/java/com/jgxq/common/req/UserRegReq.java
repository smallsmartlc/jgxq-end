package com.jgxq.common.req;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author LuCong
 * @since 2020-12-07
 **/
@Data
public class UserRegReq {

    @NotBlank(message = "昵称不能为空哦")
    private String nickName;

    @Email(message = "邮箱地址不合法")
    String email;

    @NotBlank(message = "密码不能为空")
    @Size(min=6, max=15, message="密码长度只能在6-15之间")
    String password;

}
