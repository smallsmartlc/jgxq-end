package com.jgxq.common.res;

import com.jgxq.common.dto.TeamInfos;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TeamRes {
    private Integer id;
    private String name;
    private String enName;
    private String logo;
    private TeamInfos infos;
}
