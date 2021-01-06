package com.jgxq.common.req;

import lombok.Data;

/**
 * @author LuCong
 * @since 2021-01-06
 **/
@Data
public class PasswordModifyReq {
    private String oldPwd;
    private String newPwd;
}
