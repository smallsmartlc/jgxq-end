package com.jgxq.common.res;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-11
 */
@Data
public class MatchBasicRes {

    private Integer id;
    private String title;
    private TeamBasicRes homeTeam;
    private Integer homeScore;
    private TeamBasicRes visitingTeam;
    private Integer visitingScore;
    private Date startTime;
    private String link;

}
