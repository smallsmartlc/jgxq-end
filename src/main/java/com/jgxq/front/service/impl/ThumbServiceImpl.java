package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.DeleteEnum;
import com.jgxq.front.define.ForumErrorCode;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.define.ObjectType;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.entity.News;
import com.jgxq.front.entity.Talk;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.service.ThumbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    @Autowired
    private ThumbMapper thumbMapper;


    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private TalkServiceImpl talkService;

    @Autowired
    private NewsServiceImpl newsService;

    @Override
    public Boolean thumb(Byte type, Integer id, String userkey) {
        Thumb thumb = new Thumb();
        thumb.setType(type);
        thumb.setUserkey(userkey);
        thumb.setObjectId(id);
        int flag;
        try {
            flag = thumbMapper.insert(thumb);
        }catch (DuplicateKeyException e){
            return false;
        }
        boolean res = flag > 0;
        try {
            if(res){
                if (type == InteractionType.TALK.getValue()) {
                    Talk author = talkService.getById(id);
                    if (author != null && (!userkey.equals(author.getAuthor()))) {
                        messageService.sendThumbMessage(userkey, author.getAuthor(), type, id);
                    }
                }
                if (type == InteractionType.COMMENT.getValue()) {
                    Comment comment = commentService.getById(id);

                    String author = null;
                    if (comment.getType() == ObjectType.NEWS.getValue()) {
                        News byId = newsService.getById(comment.getObjectId());
                        if (byId != null) author = byId.getAuthor();
                    }
                    if (comment.getType() == ObjectType.NEWS.getValue()) {
                        Talk byId = talkService.getById(comment.getObjectId());
                        if (byId != null) author = byId.getAuthor();
                    }
                    if (!userkey.equals(author)) {
                        messageService.sendThumbMessage(userkey, author, comment.getType(), comment.getObjectId(), comment.getContent());
                    }
                }
            }
        }catch (Exception e){
            System.err.println("消息发送异常");
        }
        return res;
    }

}
