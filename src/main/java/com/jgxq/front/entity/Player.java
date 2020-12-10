package com.jgxq.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private String enName;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 身高-厘米
     */
    private Integer height;

    /**
     * 体重-公斤
     */
    private Integer weight;

    /**
     * 国籍
     */
    private String nation;

    /**
     * 号码
     */
    private Integer number;

    private Integer team;

    /**
     * 惯用脚 0右脚,1左脚,2双脚
     */
    private Byte strongFoot;

    /**
     * 位置 门将0,后卫1,中场2,前锋3
     */
    private Byte position;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 基本信息
     */
    private String infos;

    /**
     * 1 正常,-1 已删除
     */
    private Byte status;

    private Date createTime;

    private Date updateTime;


}
