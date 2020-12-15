package com.jgxq.common.res;

import com.jgxq.common.dto.CommentHit;
import lombok.Data;

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
public class CommentUserRes {

    private Integer id;
    /**
     * 默认为0 新闻,1 - 日白
     */

    private Integer thumbs;
    /**
     * 评论
     */
    private String content;

    private Date createAt;

    private Byte type;

    private Integer objectId;

    private String title;

}
