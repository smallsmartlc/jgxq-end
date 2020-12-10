package com.jgxq.front.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.TeamInfos;
import com.jgxq.common.req.TeamReq;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.common.res.TeamRes;
import com.jgxq.core.resp.PageResponse;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.DeleteEnum;
import com.jgxq.front.define.TeamSortEnum;
import com.jgxq.front.entity.Team;
import com.jgxq.front.service.TeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @PostMapping
    public ResponseMessage addTeam(@RequestBody @Validated TeamReq teamDto) {

        String info = JSON.toJSONString(teamDto.getInfos());
        Team team = new Team();
        BeanUtils.copyProperties(teamDto, team);
        team.setInfos(info);

        teamService.save(team);

        return new ResponseMessage(team.getId());
    }

    @PutMapping("{id}")
    public ResponseMessage updateTeamInfo(@PathVariable("id") Integer id,
                                          @RequestBody @Validated TeamReq teamDto) {
        String info = JSON.toJSONString(teamDto.getInfos());
        Team team = new Team();
        BeanUtils.copyProperties(teamDto, team);
        team.setInfos(info);
        team.setId(id);
        boolean flag = teamService.updateById(team);

        return new ResponseMessage(flag);
    }

    @DeleteMapping("{id}")
    public ResponseMessage deleteTeamById(@PathVariable("id") Integer id) {
        boolean flag = teamService.removeById(id);
        return new ResponseMessage(flag);
    }

    @GetMapping("page/{pageNum}/{pageSize}")
    public ResponseMessage PageTeams(@PathVariable("pageNum") Integer pageNum,
                                     @PathVariable("pageSize") Integer pageSize,
                                     @RequestParam(value = "sort",defaultValue = "NO") TeamSortEnum sort) {

        Page<Team> page = null;
        if(sort == TeamSortEnum.NO){
            page = teamService.pageTeams(pageNum, pageSize);
        }else if(sort == TeamSortEnum.HEAT){
            page = teamService.pageTeamsByHeat(pageNum, pageSize);
        }

        List<Team> records = page.getRecords();
        List<TeamBasicRes> res = new ArrayList<>(records.size());
        records.forEach((team) -> {
            TeamBasicRes teamBasic = new TeamBasicRes();
            BeanUtils.copyProperties(team, teamBasic);
            res.add(teamBasic);
        });

        return new ResponseMessage(new PageResponse<TeamBasicRes>(res, pageNum, pageSize, page.getTotal()));

    }

    @GetMapping("{id}")
    public ResponseMessage getBasicTeamById(@PathVariable("id") Integer id) {
        TeamBasicRes team = teamService.getBasicTeamById(id);
        return new ResponseMessage(team);
    }

    @GetMapping("infos/{id}")
    public ResponseMessage getTeamById(@PathVariable("id") Integer id) {
        Team team = teamService.getById(id);
        if(team == null){
            return new ResponseMessage(team);
        }
        TeamRes teamRes = new TeamRes();
        BeanUtils.copyProperties(team, teamRes);
        TeamInfos infos = JSON.parseObject(team.getInfos(), TeamInfos.class);
        teamRes.setInfos(infos);
        return new ResponseMessage(teamRes);
    }

}