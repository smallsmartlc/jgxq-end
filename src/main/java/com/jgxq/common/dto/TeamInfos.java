package com.jgxq.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-09
 **/
@Data
public class TeamInfos {
    private List<TeamInfo> normal;
    private List<TeamInfo> contact;
    private List<ChampionInfo> champions;
}
