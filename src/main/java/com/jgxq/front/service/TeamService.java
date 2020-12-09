package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
public interface TeamService extends IService<Team> {

    Page<Team> pageTeams(Integer pageNum, Integer pageSize);

    Page<Team> pageTeamsByHeat(Integer pageNum, Integer pageSize);
}
