package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.entity.Team;
import com.jgxq.front.mapper.TeamMapper;
import com.jgxq.front.service.TeamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public Page<Team> pageTeams(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        wrapper.select("id","name","logo");

        teamMapper.selectPage(page,null);

        return page;
    }

    @Override
    public Page<Team> pageTeamsByHeat(Integer pageNum, Integer pageSize) {

        Page<Team> page = new Page<>(pageNum, pageSize);

        teamMapper.pageTeamsByHeat(page);

        return page;
    }
}
