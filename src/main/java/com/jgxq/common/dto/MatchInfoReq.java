package com.jgxq.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Data
public class MatchInfoReq {
    //主队首发
    List<MatchPlayerReq> homeLineUp;
    //主队替补
    List<MatchPlayerReq> homeSubstitute;
    //客队首发
    List<MatchPlayerReq> visitingLineUp;
    //客队替补
    List<MatchPlayerReq> visitingSubstitute;

}
