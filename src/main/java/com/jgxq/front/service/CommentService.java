package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.CommentRes;
import com.jgxq.common.res.CommentTalkRes;
import com.jgxq.common.res.CommentUserRes;
import com.jgxq.common.res.ReplyRes;
import com.jgxq.front.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
public interface CommentService extends IService<Comment> {

    Page<CommentRes> pageComment(Byte type, Integer objectId, String userKey,Integer pageNum, Integer pageSize);

    Page<CommentTalkRes> pageMainComment(Byte type, Integer objectId, String userKey, Integer pageNum, Integer pageSize);

    Page<ReplyRes> pageReply(Integer commentId, Integer pageNum, Integer pageSize, String userKey);

    Page<CommentUserRes> pageUserComment(Integer pageNum, Integer pageSize, String target, String userKey);
}
