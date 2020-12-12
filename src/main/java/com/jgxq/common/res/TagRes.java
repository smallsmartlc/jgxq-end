package com.jgxq.common.res;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author LuCong
 * @since 2020-12-12
 **/
@Data
public class TagRes {
    private List<TeamBasicRes> teams;
    private List<PlayerBasicRes> players;
}
