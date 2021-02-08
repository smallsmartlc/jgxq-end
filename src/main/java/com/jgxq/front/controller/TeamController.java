package com.jgxq.front.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.TeamInfos;
import com.jgxq.common.req.TeamReq;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.common.res.TeamRes;
import com.jgxq.core.resp.PageResponse;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.TeamSort;
import com.jgxq.front.entity.Team;
import com.jgxq.front.service.TeamService;
import com.jgxq.front.util.ReqUtils;
import org.apache.commons.lang3.StringUtils;
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
    public ResponseMessage addTeam(@RequestBody @Validated TeamReq teamReq) {

        Team team = ReqUtils.teamReqToTeam(teamReq);
        teamService.save(team);

        return new ResponseMessage(team.getId());
    }

    @PutMapping("{id}")
    public ResponseMessage updateTeamInfo(@PathVariable("id") Integer id,
                                          @RequestBody @Validated TeamReq teamReq) {
        Team team = ReqUtils.teamReqToTeam(teamReq);
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
                                     @RequestParam(value = "sort",defaultValue = "NO") TeamSort sort,
                                     @RequestParam(value = "keyword",required = false) String keyword) {
        if(!StringUtils.isEmpty(keyword)){
            Page<TeamBasicRes> page = teamService.searchTeam(pageNum, pageSize,keyword);
            return new ResponseMessage(new PageResponse<>(page.getRecords(), pageNum, pageSize, page.getTotal()));
        }
        Page<Team> page = null;
        if(sort == TeamSort.NO){
            page = teamService.pageTeams(pageNum, pageSize);
        }else if(sort == TeamSort.HEAT){
            page = teamService.pageTeamsByHeat(pageNum, pageSize);
        }

        List<Team> records = page.getRecords();
        List<TeamBasicRes> res = new ArrayList<>(records.size());
        records.forEach((team) -> {
            TeamBasicRes teamBasic = new TeamBasicRes();
            BeanUtils.copyProperties(team, teamBasic);
            res.add(teamBasic);
        });

        return new ResponseMessage(new PageResponse(res, pageNum, pageSize, page.getTotal()));

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