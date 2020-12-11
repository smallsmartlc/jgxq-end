package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.entity.Match;
import com.jgxq.front.entity.Team;
import com.jgxq.front.mapper.MatchMapper;
import com.jgxq.front.mapper.TeamMapper;
import com.jgxq.front.service.MatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jgxq.front.service.TeamService;
import com.jgxq.front.util.ResUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
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
    public List<MatchBasicRes> listMatches(Integer size, Date start) {
        QueryWrapper<Match> matchQuery = new QueryWrapper<>();
        matchQuery.select("id","title","home_team","visiting_team","start_time","link")
                .ge("start_time",start).last("limit "+size);
        List<Match> matchList = matchMapper.selectList(matchQuery);
        Set<Integer> teamIds = new HashSet<>();
        matchList.forEach(m->{
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
