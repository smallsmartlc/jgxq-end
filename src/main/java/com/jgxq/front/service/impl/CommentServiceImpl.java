package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.CommentHit;
import com.jgxq.common.dto.CommentHitDto;
import com.jgxq.common.res.CommentRes;
import com.jgxq.common.res.UserLoginRes;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.CommentMapper;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public Page<CommentRes> pageComment(Byte type, Integer objectId, String userKey, Integer pageNum, Integer pageSize) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Comment> commentQuery = new QueryWrapper<>();
        commentQuery.eq("object_id", objectId)
                .eq("type", type)
                .eq("parent_id", 0);
        Page<Comment> commentPage = commentMapper.selectPage(page, commentQuery);

        List<Comment> records = commentPage.getRecords();
        List<Integer> parentIds = records.stream()
                .map(Comment::getId).collect(Collectors.toList());
        //获取点赞和评论数
        QueryWrapper<Comment> commentNumQuery = new QueryWrapper<>();
        commentNumQuery.select("id",
                "(SELECT count(*) from `comment` as c where c.parent_id = comment.id) as comments",
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


        List<Integer> hasComent = null;
        List<Integer> hasThumb = null;
        boolean logged = !StringUtils.isBlank(userKey);
        if (logged) {
            //获取登陆用户是否点赞,评论
            QueryWrapper<Comment> hasCommentQuery = new QueryWrapper<>();
            hasCommentQuery.select("id")
                    .eq("userkey", userKey)
                    .in("id", parentIds);
            List<Comment> comments = commentMapper.selectList(hasCommentQuery);
            hasComent = comments.stream().map(Comment::getObjectId).collect(Collectors.toList());
            QueryWrapper<Thumb> hasThumbQuery = new QueryWrapper<>();
            hasCommentQuery.select("object_id")
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
        List<Integer> finalHasComent = hasComent;//这两行为什么要这么做改天要研究一下
        List<CommentRes> result = records.stream().map(c -> {
            CommentHit hit = new CommentHit();
            Integer id = c.getId();
            CommentHitDto commentHitDto = hitMap.get(id);
            hit.setComments(commentHitDto.getComments());
            hit.setThumbs(commentHitDto.getThumbs());
            if (logged) {
                hit.setThumb(finalHasThumb.contains(id));
                hit.setComment(finalHasComent.contains(id));
            }
            //TODO : 用户信息赋值
            CommentRes commentRes = new CommentRes();
            BeanUtils.copyProperties(c, commentRes);
            commentRes.setUserkey(userMap.get(c.getUserkey()));
            commentRes.setHits(hit);
            return commentRes;
        }).collect(Collectors.toList());

        Page<CommentRes> resPage = new Page<>(commentPage.getCurrent(), commentPage.getSize(), commentPage.getTotal());
        resPage.setRecords(result);

        return resPage;
    }

}
