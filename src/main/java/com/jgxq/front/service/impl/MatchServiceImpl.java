package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.entity.Match;
import com.jgxq.front.mapper.MatchMapper;
import com.jgxq.front.service.MatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * @since 2020-12-11
 */
@Service
public class MatchServiceImpl extends ServiceImpl<MatchMapper, Match> implements MatchService {

    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private TeamServiceImpl teamService;

    @Override
    public Page<MatchBasicRes> listMatches(Date start, String teamId, Integer pageNum, Integer pageSize) {
        QueryWrapper<Match> matchQuery = new QueryWrapper<>();

        matchQuery.select("id", "title", "home_team", "visiting_team", "start_time", "link","home_score","visiting_score")
                .ge("start_time", start);
        if(teamId!=null){
            matchQuery.eq("home_team",teamId).or().eq("visiting_team",teamId);
        }
        Page page = new Page();
        matchMapper.selectPage(page,matchQuery);
        List<MatchBasicRes> res = matchListToBasicRes(page.getRecords());
        Page<MatchBasicRes> resPage = new Page<>(page.getCurrent(),page.getSize(),page.getTotal());
        resPage.setRecords(res);
        return resPage;
    }

    @Override
    public List<MatchBasicRes> homeMatches(Integer size, String teamId) {
        QueryWrapper<Match> matchQuery = new QueryWrapper<>();
        matchQuery.select("id", "title", "home_team", "visiting_team", "start_time", "link","home_score","visiting_score")
                .orderByAsc("abs(TIMESTAMPDIFF(SECOND,start_time,now()))").last("limit " + size);
        if(teamId!=null){
            matchQuery.eq("home_team",teamId).or().eq("visiting_team",teamId);
        }
        List<Match> matchList = matchMapper.selectList(matchQuery);
        List<MatchBasicRes> res = matchListToBasicRes(matchList);
        return res;
    }

    private List<MatchBasicRes> matchListToBasicRes(List<Match> matchList){
        Set<Integer> teamIds = new HashSet<>();
        matchList.forEach(m -> {
            teamIds.add(m.getHomeTeam());
            teamIds.add(m.getVisitingTeam());
        });
        List<TeamBasicRes> teamList = teamService.getBasicTeamByIds(teamIds);
        Map<Integer, TeamBasicRes> map = teamList
                .stream().collect(Collectors.toMap(TeamBasicRes::getId, v -> v));
        List<MatchBasicRes> res = matchList.stream().map(t -> {
            MatchBasicRes match = new MatchBasicRes();
            BeanUtils.copyProperties(t, match);
            match.setHomeTeam(map.get(t.getHomeTeam()));
            match.setVisitingTeam(map.get(t.getVisitingTeam()));
            return match;
        }).collect(Collectors.toList());
        return res;
    }
}
