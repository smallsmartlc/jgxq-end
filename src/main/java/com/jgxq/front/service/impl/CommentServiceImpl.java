package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.CommentHit;
import com.jgxq.common.dto.CommentHitDto;
import com.jgxq.common.res.CommentRes;
import com.jgxq.common.res.CommentUserRes;
import com.jgxq.common.res.ReplyRes;
import com.jgxq.common.res.UserLoginRes;
import com.jgxq.front.define.ObjectType;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.CommentMapper;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ThumbMapper thumbMapper;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private TalkServiceImpl talkService;

    @Override
    public Page<CommentRes> pageComment(Byte type, Integer objectId, String userKey, Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> commentQuery = new QueryWrapper<>();
        commentQuery.eq("object_id", objectId)
                .eq("type", type)
                .eq("parent_id", 0);
        commentMapper.selectPage(page, commentQuery);

        List<Comment> records = page.getRecords();

        if (records.isEmpty()) {
            return new Page(page.getCurrent(),page.getSize(),page.getTotal());
        }

        List<Integer> parentIds = records.stream()
                .map(Comment::getId).collect(Collectors.toList());
        //获取点赞和评论数
        QueryWrapper<Comment> commentNumQuery = new QueryWrapper<>();
        commentNumQuery.select("id",
                "(SELECT count(*) from `comment` as c where c.parent_id = comment.id and status = 1) as comments",
                "(SELECT count(*) from thumb where type = " + InteractionType.COMMENT.getValue() + " and object_id = comment.id) as thumbs")
                .in("id", parentIds);
        List<Map<String, Object>> commentNums = commentMapper.selectMaps(commentNumQuery);
        Map<Integer, CommentHitDto> hitMap = commentNums.stream().map(m -> {
            CommentHitDto commentHit = new CommentHitDto();
            String id = m.get("id").toString();
            String comments = m.get("comments").toString();
            String thumbs = m.get("thumbs").toString();
            commentHit.setId(Integer.parseInt(id));
            commentHit.setComments(Integer.parseInt(comments));
            commentHit.setThumbs(Integer.parseInt(thumbs));
            return commentHit;
        }).collect(Collectors.toMap(CommentHitDto::getId, c -> c));


        List<Integer> hasThumb = null;
        boolean logged = userKey != null;
        if (logged) {
            //获取登陆用户是否点赞
            QueryWrapper<Thumb> hasThumbQuery = new QueryWrapper<>();
            hasThumbQuery.select("object_id")
                    .eq("userkey", userKey)
                    .eq("type", InteractionType.COMMENT.getValue())
                    .in("object_id", parentIds);
            List<Thumb> thumbs = thumbMapper.selectList(hasThumbQuery);
            hasThumb = thumbs.stream().map(Thumb::getObjectId).collect(Collectors.toList());
        }

        // 通过评论的userkey获取用户
        Set<String> userKeyList = records.stream().map(Comment::getUserkey).collect(Collectors.toSet());
        List<UserLoginRes> userInfos = userService.getUserInfoByKeyList(userKeyList);
        Map<String, UserLoginRes> userMap = userInfos.stream().collect(Collectors.toMap(UserLoginRes::getUserkey, u -> u));


        List<Integer> finalHasThumb = hasThumb;
        List<CommentRes> result = records.stream().map(c -> {
            CommentHit hit = new CommentHit();
            Integer id = c.getId();
            CommentHitDto commentHitDto = hitMap.get(id);
            hit.setComments(commentHitDto.getComments());
            hit.setThumbs(commentHitDto.getThumbs());
            if (logged) {
                hit.setThumb(finalHasThumb.contains(id));
            }
            CommentRes commentRes = new CommentRes();
            BeanUtils.copyProperties(c, commentRes);
            commentRes.setUserkey(userMap.get(c.getUserkey()));
            commentRes.setHits(hit);
            return commentRes;
        }).collect(Collectors.toList());

        Page<CommentRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(result);

        return resPage;
    }

    @Override
    public Page<ReplyRes> pageReply(Integer commentId, Integer pageNum, Integer pageSize, String userKey) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> commentQuery = new QueryWrapper<>();
        commentQuery.eq("parent_id", commentId);
        Page<Comment> commentPage = commentMapper.selectPage(page, commentQuery);

        List<Comment> records = commentPage.getRecords();

        if (records.isEmpty()) {
            return null;
        }
        // 获取评论里回复的回复
        List<Integer> replyIds = records.stream()
                .map(Comment::getReplyId).collect(Collectors.toList());
        List<Comment> replyComment = null;
        if (!replyIds.isEmpty()) {
            QueryWrapper<Comment> replyQuery = new QueryWrapper<>();
            replyQuery.in("reply_id", replyIds);
            replyComment = commentMapper.selectList(replyQuery);
        } else {
            replyIds = Collections.EMPTY_LIST;
        }
        Map<Integer, Comment> replyMap = replyComment.stream().collect(Collectors.toMap(Comment::getId, c -> c));


        List<Integer> commentIds = records.stream()
                .map(Comment::getId).collect(Collectors.toList());
        //获取点赞数
        QueryWrapper<Comment> thumbNumQuery = new QueryWrapper<>();
        thumbNumQuery.select("id",
                "(SELECT count(*) from thumb where type = " + InteractionType.COMMENT.getValue() + " and object_id = comment.id) as thumbs")
                .in("id", commentIds);
        List<Map<String, Object>> thumbNums = commentMapper.selectMaps(thumbNumQuery);
        Map<Integer, Integer> thumbsMap = thumbNums
                .stream().collect(Collectors.toMap(m -> Integer.parseInt(m.get("id").toString()),
                        m -> Integer.parseInt(m.get("thumbs").toString())));

        List<Integer> hasThumb = null;
        boolean logged = userKey != null;
        if (logged) {
            //获取登陆用户是否点赞,评论
            QueryWrapper<Thumb> hasThumbQuery = new QueryWrapper<>();
            hasThumbQuery.select("object_id")
                    .eq("userkey", userKey)
                    .eq("type", InteractionType.COMMENT.getValue())
                    .in("object_id", commentIds);
            List<Thumb> thumbs = thumbMapper.selectList(hasThumbQuery);
            hasThumb = thumbs.stream().map(Thumb::getObjectId).collect(Collectors.toList());
        }

        // 通过评论的userkey获取用户
        Set<String> userKeyList = records.stream().map(Comment::getUserkey).collect(Collectors.toSet());
        Set<String> replyUserKetList = replyComment.stream().map(Comment::getUserkey).collect(Collectors.toSet());
        userKeyList.addAll(replyUserKetList);
        List<UserLoginRes> userInfos = userService.getUserInfoByKeyList(userKeyList);
        Map<String, UserLoginRes> userMap = userInfos.stream().collect(Collectors.toMap(UserLoginRes::getUserkey, u -> u));


        List<Integer> finalHasThumb = hasThumb;
        List<ReplyRes> result = records.stream().map(c -> {
            ReplyRes replyRes = new ReplyRes();
            Integer id = c.getId();
            Integer commentThumbs = thumbsMap.get(id);
            if (logged) {
                replyRes.setThumb(finalHasThumb.contains(id));
            }
            BeanUtils.copyProperties(c, replyRes);
            replyRes.setUserkey(userMap.get(c.getUserkey()));
            replyRes.setThumbs(commentThumbs);
            if (c.getReplyId() != 0) {
                Comment comment = replyMap.get(c.getReplyId());
                CommentRes thisReply = new CommentRes();
                if (comment != null) {
                    BeanUtils.copyProperties(comment, thisReply);
                    thisReply.setUserkey(userMap.get(comment.getUserkey()));
                }
                replyRes.setReply(thisReply);
            }
            return replyRes;
        }).collect(Collectors.toList());

        Page<ReplyRes> resPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        resPage.setRecords(result);

        return resPage;


    }

    @Override
    public Page<CommentUserRes> pageUserComment(Integer pageNum, Integer pageSize, String target, String userKey) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> commentQuery = new QueryWrapper<>();
        commentQuery.eq("userkey", target);
        Page<Comment> commentPage = commentMapper.selectPage(page, commentQuery);

        List<Comment> records = commentPage.getRecords();

        if (records.isEmpty()) {
            return null;
        }

        List<Integer> commentIds = records.stream()
                .map(Comment::getId).collect(Collectors.toList());
        //获取点赞数
        QueryWrapper<Comment> thumbNumQuery = new QueryWrapper<>();
        thumbNumQuery.select("id",
                "(SELECT count(*) from thumb where type = " + InteractionType.COMMENT.getValue() + " and object_id = comment.id) as thumbs")
                .in("id", commentIds);
        List<Map<String, Object>> thumbNums = commentMapper.selectMaps(thumbNumQuery);
        Map<Integer, Integer> thumbsMap = thumbNums
                .stream().collect(Collectors.toMap(m -> Integer.parseInt(m.get("id").toString()),
                        m -> Integer.parseInt(m.get("thumbs").toString())));

        Set<Integer> talkIds = records.stream()
                .filter(c -> c.getType() == ObjectType.TALK.getValue())
                .map(c -> c.getObjectId()).collect(Collectors.toSet());
        Set<Integer> newsIds = records.stream()
                .filter(c -> c.getType() == ObjectType.NEWS.getValue())
                .map(c -> c.getObjectId()).collect(Collectors.toSet());

        Map<Integer, String> talkMap = null;
        if (talkIds.isEmpty()) {
            talkMap = Collections.EMPTY_MAP;
        } else {
            talkMap = talkService.listByIds(talkIds).stream()
                    .collect(Collectors.toMap(t -> t.getId(), t -> StringUtils.abbreviate(t.getText(), 60)));
        }

        Map<Integer, String> newsMap = null;
        if (newsIds.isEmpty()) {
            newsMap = Collections.EMPTY_MAP;
        } else {
            newsMap = newsService.listByIds(newsIds).stream()
                    .collect(Collectors.toMap(n -> n.getId(), n -> StringUtils.abbreviate(n.getTitle(), 60)));
        }
        Map<Integer, String> finalNewsMap = newsMap;
        Map<Integer, String> finalTalkMap = talkMap;
        List<CommentUserRes> result = records.stream().map(c -> {
            CommentUserRes commentRes = new CommentUserRes();
            Integer id = c.getId();
            Integer commentThumbs = thumbsMap.get(id);
            BeanUtils.copyProperties(c, commentRes);
            commentRes.setThumbs(commentThumbs);
            if (c.getType() == ObjectType.NEWS.getValue()) {
                commentRes.setTitle(finalNewsMap.get(c.getObjectId()));
            }
            if (c.getType() == ObjectType.TALK.getValue()) {
                commentRes.setTitle(finalTalkMap.get(c.getObjectId()));
            }
            return commentRes;
        }).collect(Collectors.toList());

        Page<CommentUserRes> resPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        resPage.setRecords(result);

        return resPage;
    }

}
