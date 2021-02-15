package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.common.utils.DateUtils;
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
            matchQuery.nested(i->i.eq("home_team",teamId).or().eq("visiting_team",teamId));
        }
        Page page = new Page(pageNum,pageSize);
        matchMapper.selectPage(page,matchQuery);
        List<Match> matchList = page.getRecords();
        if(matchList.isEmpty() && DateUtils.sameDate(new Date(),start)){
            QueryWrapper<Match> tempQuery = new QueryWrapper<>();
            tempQuery.lt("start_time",start).last("limit "+pageSize);
            if(teamId!=null){
                tempQuery.eq("home_team",teamId).or().eq("visiting_team",teamId);
            }
            matchList = matchMapper.selectList(tempQuery);
        }
        List<MatchBasicRes> res = matchListToBasicRes(matchList);
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

    @Override
    public List<MatchBasicRes> endMatches(Integer size) {
        QueryWrapper<Match> matchQuery = new QueryWrapper<>();
        matchQuery.le("start_time", new Date()).orderByDesc("start_time").last("limit " + size);
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
        TeamBasicRes delete = new TeamBasicRes();
        delete.setName("已删除");
        List<MatchBasicRes> res = matchList.stream().map(t -> {
            MatchBasicRes match = new MatchBasicRes();
            BeanUtils.copyProperties(t, match);
            TeamBasicRes homeTeam = map.get(t.getHomeTeam());
            TeamBasicRes visitingTeam = map.get(t.getVisitingTeam());
            if(homeTeam == null){
                homeTeam = delete;
            }
            if(visitingTeam == null){
                visitingTeam = delete;
            }
            match.setHomeTeam(homeTeam);
            match.setVisitingTeam(visitingTeam);
            return match;
        }).collect(Collectors.toList());
        return res;
    }
}
