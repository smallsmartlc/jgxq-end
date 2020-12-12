package com.jgxq.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.jgxq.common.dto.TagInfo;
import com.jgxq.common.res.PlayerBasicRes;
import com.jgxq.common.res.TagRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.News;
import com.jgxq.front.mapper.NewsMapper;
import com.jgxq.front.service.NewsService;
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
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private PlayerServiceImpl playerService;

    public TagRes tagInfosToRes(String tagSetStr){
        List<TagInfo> tagInfos = JSON.parseArray(tagSetStr, TagInfo.class);

        Map<Integer, List<TagInfo>> map = tagInfos.stream().collect(Collectors.groupingBy(tagInfo -> tagInfo.getType()));
        TagRes tagRes = new TagRes();
        List<TagInfo> teams = map.get(TagType.TEAM.getValue());
        List<TagInfo> players = map.get(TagType.PLAYER.getValue());
        if(teams!=null){
            List<Integer> teamIds = teams.stream().map(TagInfo::getObjectId).collect(Collectors.toList());
            List<TeamBasicRes> teamList = teamService.getBasicTeamByIds(teamIds);
            tagRes.setTeams(teamList);
        }
        if(players!=null){
            List<Integer> playerIds = players.stream().map(TagInfo::getObjectId).collect(Collectors.toList());
            List<PlayerBasicRes> playerList = playerService.geyBasicByIds(playerIds);
            tagRes.setPlayers(playerList);
        }
        return tagRes;
    }

}
