package com.jgxq.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Thumb implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * NEWS(0),TALK(1),COMMENT(2);
     */
    private Byte type;

    private Integer objectId;

    /**
     * 点赞人
     */
    private String userkey;

    @TableLogic
    private Byte status;

    private Date createTime;


}
