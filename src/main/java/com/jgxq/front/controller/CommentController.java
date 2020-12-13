package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.req.CommentReq;
import com.jgxq.common.res.CommentRes;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.service.CommentService;
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

    @PostMapping
    public ResponseMessage comment(@RequestAttribute("userKey") String userkey,
                                   @RequestBody @Validated CommentReq commentReq) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentReq, comment);
        comment.setUserkey(userkey);

        boolean save = commentService.save(comment);

        return new ResponseMessage(save);
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
                                        @RequestAttribute("userKey") String userKey,
                                        @PathVariable("pageNum") Integer pageNum,
                                        @PathVariable("pageSize") Integer pageSize) {
        Page<CommentRes> page = commentService.pageComment(type,objectId,userKey,pageNum,pageSize);

        return new ResponseMessage(page);
    }

}
