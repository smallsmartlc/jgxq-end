package com.jgxq.common.req;

import com.jgxq.common.dto.ActionInfoReq;
import com.jgxq.common.dto.ActionReq;
import com.jgxq.common.dto.MatchInfoReq;
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
    @NotNull
    private Integer visitingTeam;
    @NotNull
    private Date startTime;
    private String link;
    private Integer matchNews;
    private MatchInfoReq matchInfo;
    private List<ActionReq> action;

}
