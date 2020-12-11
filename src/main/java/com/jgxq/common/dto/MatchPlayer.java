package com.jgxq.common.dto;

import lombok.Data;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
public class MatchPlayer {

    private Integer playerId;
    //比赛站位
    private Integer matchPos;

    private String name;

    private Integer number;

}
