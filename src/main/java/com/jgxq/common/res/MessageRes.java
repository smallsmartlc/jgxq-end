package com.jgxq.common.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
@Data
public class MessageRes {

    private Integer id;

    private Byte messageType;

    private Byte objectType;

    private Integer objectId;

    private UserBasicRes user;

    /**
     * 未读为1,已读为0
     */
    private Byte read;
    /**
     * 内容
     */
    private String text;

    private Date createAt;

}
