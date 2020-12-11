package com.jgxq.common.res;

import com.jgxq.common.dto.PlayerInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-10
 */
@Data
public class MatchPlayer {
    private Integer id;
    private String name;
    private Integer number;
}
