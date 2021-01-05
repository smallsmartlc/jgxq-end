package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.req.CommentReq;
import com.jgxq.common.res.CommentRes;
import com.jgxq.common.res.CommentUserRes;
import com.jgxq.common.res.ReplyRes;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.ObjectType;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.service.CommentService;
import com.jgxq.front.service.impl.MessageServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@RestController
@RequestMapping("/comment")
@UserPermissionConf
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MessageServiceImpl messageService;

    @PostMapping
    public ResponseMessage comment(@RequestAttribute("userKey") String userkey,
                                   @RequestBody @Validated CommentReq commentReq) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentReq, comment);
        comment.setUserkey(userkey);

        commentService.save(comment);

        try {
            if (commentReq.getTarget()!=null && !userkey.equals(commentReq.getTarget())) {
                messageService.sendCommentMessage(userkey, commentReq.getTarget(), commentReq.getType(), commentReq.getObjectId(), commentReq.getContent());
            }
        }catch (Exception e){
            System.err.println("消息发送异常");
        }
        return new ResponseMessage(comment.getId());
    }

    @DeleteMapping("{id}")
    public ResponseMessage deleteComment(@RequestAttribute("userKey") String userkey,
                                         @PathVariable("id") Integer id) {
        UpdateWrapper<Comment> commentUpdate = new UpdateWrapper<>();
        commentUpdate.eq("userkey", userkey)
                .eq("id", id);
        boolean remove = commentService.remove(commentUpdate);
        return new ResponseMessage(remove);
    }

    @GetMapping("{type}/{objId}/{pageNum}/{pageSize}")
    @AllowAccess
    private ResponseMessage pageComment(@PathVariable(value = "type") Byte type,
                                        @PathVariable(value = "objId") Integer objectId,
                                        @RequestAttribute(value = "userKey", required = false) String userKey,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) {
        Page<CommentRes> page = commentService.pageComment(type, objectId, userKey, pageNum, pageSize);

        return new ResponseMessage(page);
    }

    @GetMapping("reply/{commentId}/{pageNum}/{pageSize}")
    private ResponseMessage pageReply(@PathVariable(value = "commentId") Integer commentId,
                                      @PathVariable("pageNum") Integer pageNum,
                                      @PathVariable("pageSize") Integer pageSize,
                                      @RequestAttribute(value = "userKey") String userKey) {
        Page<ReplyRes> page = commentService.pageReply(commentId, pageNum, pageSize, userKey);

        return new ResponseMessage(page);
    }

    @GetMapping("page/user")
    public ResponseMessage pageComment(@RequestParam(value = "target", required = false) String target,
                                       @RequestAttribute(value = "userKey") String userKey,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        if (target == null) {
            target = userKey;
        }
        Page<CommentUserRes> page = commentService.pageUserComment(pageNum, pageSize, target, userKey);

        return new ResponseMessage(page);
    }

}
