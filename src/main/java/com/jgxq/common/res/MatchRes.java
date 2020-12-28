package com.jgxq.common.res;

import com.jgxq.common.dto.Action;
import com.jgxq.common.dto.MatchInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-11
 */
@Data
public class MatchRes{

    private Integer id;
    private String title;
    private TeamBasicRes homeTeam;
    private TeamBasicRes visitingTeam;
    private Date startTime;
    private NewsBasicRes matchNews;
    private MatchInfo matchInfo;
    private String link;
    private List<Action> actions;
    private Integer homeScore;
    private Integer visitingScore;

}
