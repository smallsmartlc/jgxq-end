package com.jgxq.common.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jgxq.common.dto.PlayerInfo;
import com.jgxq.front.entity.Player;
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
 * @since 2020-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerReq {

    @NotBlank
    private String name;

    @NotBlank
    private String enName;

    @NotBlank
    private String headImage;

    @NotNull
    private Integer height;

    /**
     * 体重-公斤
     */
    @NotNull
    private Integer weight;

    /**
     * 国籍
     */
    @NotBlank
    private String nation;

    /**
     * 号码
     */
    @NotNull
    private Integer number;

    private Integer team;

    /**
     * 惯用脚 0右脚,1左脚,2双脚
     */
    private Byte strongFoot;

    /**
     * 位置 门将0,后卫1,中场2,前锋3
     */
    @NotNull
    private Byte position;

    /**
     * 生日
     */
    @NotNull
    private Date birthday;

    /**
     * 基本信息
     */
    private List<PlayerInfo> infos;

}
