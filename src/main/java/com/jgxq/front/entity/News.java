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
 * @since 2020-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题-64个字
     */
    private String title;

    /**
     * 封面图片
     */
    private String cover;

    /**
     * 作者userkey
     */
    private String author;

    /**
     * 文章内容
     */
    private String text;

    /**
     * 存储tag
     */
    private String tag;

    private Byte status;

    private Date createAt;

    private Date updateAt;


}
