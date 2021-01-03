package com.jgxq.common.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jgxq.common.dto.PlayerInfo;
import com.jgxq.common.dto.PlayerInfos;
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
@EqualsAndHashCode(callSuper = false)
public class PlayerRes implements Serializable {
    private Integer id;
    private String name;
    private String enName;
    private String headImage;
    private Integer height;
    private Integer weight;
    private String nation;
    private Integer number;
    private TeamBasicRes team;
    private String strongFoot;
    private String position;
    private Date birthday;
    private PlayerInfos infos;


}
