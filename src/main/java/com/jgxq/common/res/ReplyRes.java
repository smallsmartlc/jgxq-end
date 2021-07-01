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
public class ReplyRes {
    private Integer id;

    private UserRes userkey;

    private Integer thumbs;

    private boolean thumb;
    /**
     * 评论
     */
    private Byte type;

    private Integer objectId;

    private Integer parentId;

    private String content;

    private Date createAt;

    private CommentRes reply;

}
