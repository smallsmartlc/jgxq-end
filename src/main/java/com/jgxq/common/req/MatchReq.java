package com.jgxq.common.req;

import com.jgxq.common.dto.Action;
import com.jgxq.common.dto.MatchInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
public class MatchReq implements Serializable {

    @NotBlank
    private String title;
    @NotNull
    private Integer homeTeam;
    private Integer homeScore;
    @NotNull
    private Integer visitingTeam;
    private Integer visitingScore;
    @NotNull
    private Date startTime;
    private String link;
    private Integer matchNews;
    private MatchInfo matchInfo;
    private List<Action> action;

}
