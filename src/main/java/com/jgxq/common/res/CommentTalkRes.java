package com.jgxq.common.res;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Data
public class CommentTalkRes {
    private Integer id;
    /**
     * 默认为0 新闻,1 - 日白
     */

    private UserRes userkey;
    /**
     * 评论
     */
    private String content;

    private Byte type;

    private Integer objectId;

    private Date createAt;

    private List<ReplyTalkRes> replyList;
}
