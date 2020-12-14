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
 * @since 2020-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Talk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者userkey
     */
    private String author;

    private String text;

    private Byte status;

    private Date createAt;

    private Date updateAt;


}
