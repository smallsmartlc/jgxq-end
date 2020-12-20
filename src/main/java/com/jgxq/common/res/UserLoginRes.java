package com.jgxq.common.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRes {

    private String userkey;

    private String nickName;

    private String headImage;

    private String city;

    private Date createAt;

    private TeamBasicRes homeTeam;

}
