package com.jgxq.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.req.TagReq;
import com.jgxq.common.res.PlayerBasicRes;
import com.jgxq.common.res.TagRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.Tag;
import com.jgxq.front.mapper.TagMapper;
import com.jgxq.front.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;


    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private PlayerServiceImpl playerService;

    @Override
    public TagRes getTags(Integer newsId){
        QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
        tagQuery.eq("news_id",newsId);
        List<Tag> tags = tagMapper.selectList(tagQuery);

        Map<Integer, List<Tag>> map = tags.stream().collect(Collectors.groupingBy(tag -> tag.getObjectType().intValue()));
        TagRes tagRes = new TagRes();
        List<Tag> teams = map.get(TagType.TEAM.getValue());
        List<Tag> players = map.get(TagType.PLAYER.getValue());
        if(teams!=null){
            List<Integer> teamIds = teams.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<TeamBasicRes> teamList = teamService.getBasicTeamByIds(teamIds);
            tagRes.setTeams(teamList);
        }
        if(players!=null){
            List<Integer> playerIds = players.stream().map(Tag::getObjectId).collect(Collectors.toList());
            List<PlayerBasicRes> playerList = playerService.geyBasicByIds(playerIds);
            tagRes.setPlayers(playerList);
        }
        return tagRes;
    }

}
