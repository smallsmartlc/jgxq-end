package com.jgxq.common.res;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jgxq.common.dto.CommentHit;
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
 * @since 2020-12-12
 */
@Data
public class CommentRes{
    private Integer id;
    /**
     * 默认为0 新闻,1 - 日白
     */

    private UserLoginRes userkey;

    private CommentHit hits;
    /**
     * 评论
     */
    private String content;

    private Byte type;

    private Integer objectId;

    private Date createAt;

}
