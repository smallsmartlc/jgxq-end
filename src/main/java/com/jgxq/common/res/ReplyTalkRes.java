package com.jgxq.common.res;

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
public class ReplyTalkRes {
    private Integer id;

    private UserRes userkey;
    /**
     * 评论
     */
    private Byte type;

    private Integer objectId;

    private Integer parentId;

    private Integer replyId;

    private String content;

    private Date createAt;

    private UserRes reply;

}
