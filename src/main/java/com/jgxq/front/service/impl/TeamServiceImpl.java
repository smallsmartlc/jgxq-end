package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.define.DeleteEnum;
import com.jgxq.front.entity.Team;
import com.jgxq.front.mapper.TeamMapper;
import com.jgxq.front.mapper.UserMapper;
import com.jgxq.front.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<Team> pageTeams(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.select("id","name","logo")
                .eq("status", DeleteEnum.NORMAL.getValue());

        teamMapper.selectPage(page,null);

        return page;
    }

    @Override
    public Page<Team> pageTeamsByHeat(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);

        QueryWrapper<Team> userQuery = new QueryWrapper<>();
        userQuery.select("id","name","logo"
                ,"(SELECT count(id) from user as u where team.id = u.home_team) as fans ")
                .eq("status", DeleteEnum.NORMAL.getValue())
                .orderByDesc("fans");
        teamMapper.selectPage(page, userQuery);
        return page;
    }

    @Override
    public TeamBasicRes getBasicTeamById(Integer id) {
        Team team = teamMapper.selectById(id);
        TeamBasicRes teamRes = new TeamBasicRes();
        BeanUtils.copyProperties(team,teamRes);
        return teamRes;
    }
}
