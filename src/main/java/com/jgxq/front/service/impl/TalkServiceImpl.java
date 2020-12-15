package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.dto.TalkHit;
import com.jgxq.front.define.ObjectType;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.entity.Talk;
import com.jgxq.front.mapper.TalkMapper;
import com.jgxq.front.service.TalkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-14
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements TalkService {

    @Autowired
    private TalkMapper talkMapper;

    @Override
    public TalkHit getHit(Integer id, String userKey) {
        QueryWrapper<Talk> talkQuery = new QueryWrapper<>();
        talkQuery.select("id",
                "(select count(*) from comment where comment.object_id = talk.id and comment.type = " + ObjectType.TALK.getValue() + ") as comments",
                "(select count(*) from thumb where thumb.object_id = talk.id and thumb.type = " + InteractionType.TALK.getValue() + ") as thumbs",
                "(select count(*) from thumb where thumb.object_id = talk.id and thumb.type = " + InteractionType.TALK.getValue() + " and thumb.userkey = '" + userKey + "') as has_thumb",
                "(select count(*) from collect where collect.obj_id = talk.id and collect.type = " + ObjectType.TALK.getValue() + " and collect.userkey = '" + userKey + "') as has_collect"
        ).eq("id", id);
        List<Map<String, Object>> maps = talkMapper.selectMaps(talkQuery);
        if (maps.isEmpty()) {
            return null;
        }
        Map<String, Object> map = maps.get(0);
        TalkHit hit = new TalkHit();
        hit.setComments(Integer.parseInt(map.get("comments").toString()));
        hit.setThumbs(Integer.parseInt(map.get("thumbs").toString()));
        hit.setThumb(Integer.parseInt(map.get("has_thumb").toString()) > 0);
        hit.setCollect(Integer.parseInt(map.get("has_collect").toString()) > 0);
        return hit;
    }

    @Override
    public Map<Integer, TalkHit> getHit(Collection<Integer> ids, String userKey) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        boolean logged = userKey != null;
        QueryWrapper<Talk> talkQuery = new QueryWrapper<>();
        String[] selectSql = new String[4];
        selectSql[0] = "id";
        selectSql[1] = "(select count(*) from comment where comment.object_id = talk.id and comment.type = " + ObjectType.TALK.getValue() + ") as comments";
        selectSql[2] = "(select count(*) from thumb where thumb.object_id = talk.id and thumb.type = " + InteractionType.TALK.getValue() + ") as thumbs";
        if (logged) {
            selectSql[3] = "(select count(*) from thumb where thumb.object_id = talk.id and thumb.type = " + InteractionType.TALK.getValue() + " and thumb.userkey = '" + userKey + "') as has_thumb";
        }
        talkQuery.select(selectSql);
        talkQuery.in("id", ids);
        List<Map<String, Object>> maps = talkMapper.selectMaps(talkQuery);
        Map<Integer, TalkHit> res = maps.stream().collect(Collectors.toMap(m -> Integer.parseInt(m.get("id").toString()), m -> {
            TalkHit hit = new TalkHit();
            hit.setComments(Integer.parseInt(m.get("comments").toString()));
            hit.setThumbs(Integer.parseInt(m.get("thumbs").toString()));
            if (logged) {
                hit.setThumb(Integer.parseInt(m.get("has_thumb").toString()) > 0);
            }
            return hit;
        }));
        return res;
    }

}
