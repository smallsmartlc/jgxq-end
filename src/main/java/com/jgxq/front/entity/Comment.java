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
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 默认为0 新闻,1 - 日白
     */
    private Byte type;

    private Integer objectId;

    private String userkey;

    /**
     * 根评论id
     */
    private Integer parentId;

    /**
     * 回复id
     */
    private Integer replyId;

    /**
     * 评论
     */
    private String content;

    private Byte status;

    private Date createAt;

    private Date updateAt;


}
