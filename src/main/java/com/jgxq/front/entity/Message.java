package com.jgxq.front.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0-赞,1-评论
     */
    private Byte messageType;
    private Byte objectType;

    private Integer objectId;

    /**
     * 发送者
     */
    private String userkey;

    /**
     * 目标
     */
    private String target;

    /**
     * 未读为1,已读为0
     */

    @TableField("`read`")
    private Byte read;

    /**
     * 内容
     */
    private String text;

    private Byte status;

    private Date createAt;

    private Date updateAt;


}
