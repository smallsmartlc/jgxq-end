package com.jgxq.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
public class MatchInfo {
    //主队首发
    List<MatchPlayer> homeLineUp;
    //主队替补
    List<MatchPlayer> homeSubstitute;
    //客队首发
    List<MatchPlayer> visitingLineUp;
    //客队替补
    List<MatchPlayer> visitingSubstitute;

}
