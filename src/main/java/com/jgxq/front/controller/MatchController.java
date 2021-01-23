package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.req.MatchReq;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.common.res.MatchRes;
import com.jgxq.common.res.NewsBasicRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.common.utils.DateUtils;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Match;
import com.jgxq.front.entity.News;
import com.jgxq.front.service.MatchService;
import com.jgxq.front.service.TeamService;
import com.jgxq.front.service.impl.MatchServiceImpl;
import com.jgxq.front.service.impl.NewsServiceImpl;
import com.jgxq.front.service.impl.TeamServiceImpl;
import com.jgxq.front.util.ReqUtils;
import com.jgxq.front.util.ResUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-11
 */
@RestController
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private NewsServiceImpl newsService;

    @PostMapping
    public ResponseMessage addMatch(@RequestBody @Validated MatchReq matchReq) {

        Match match = ReqUtils.matchReqToMatch(matchReq);
        matchService.save(match);

        return new ResponseMessage(match.getId());
    }

    @PutMapping("{id}")
    public ResponseMessage updateMatch(@PathVariable("id") Integer id,
                                       @RequestBody @Validated MatchReq matchReq) {

        Match match = ReqUtils.matchReqToMatch(matchReq);
        match.setId(id);
        boolean flag = matchService.updateById(match);

        return new ResponseMessage(flag);
    }

    @DeleteMapping("{id}")
    public ResponseMessage deleteMatch(@PathVariable("id") Integer id) {
        boolean remove = matchService.removeById(id);
        return new ResponseMessage(remove);
    }

    @GetMapping("{id}")
    public ResponseMessage getMatch(@PathVariable("id") Integer id) {
        Match match = matchService.getById(id);
        if (match == null) {
            return new ResponseMessage(match);
        }
        MatchRes res = ResUtils.matchToMatchRes(match);

        NewsBasicRes newsBasic = null;
        if (match.getMatchNews() != null) {
            News news = newsService.getById(match.getMatchNews());
            if(news != null){
                newsBasic = new NewsBasicRes();
                BeanUtils.copyProperties(news, newsBasic);
            }
        }
        res.setMatchNews(newsBasic);

        TeamBasicRes homeTeam = teamService.getBasicTeamById(match.getHomeTeam());
        TeamBasicRes visitingTeam = teamService.getBasicTeamById(match.getVisitingTeam());
        TeamBasicRes delete = new TeamBasicRes();
        delete.setName("已删除");
        if (homeTeam == null) {
            homeTeam = delete;
        }
        if (visitingTeam == null) {
            visitingTeam = delete;
        }
        res.setHomeTeam(homeTeam);
        res.setVisitingTeam(visitingTeam);

        return new ResponseMessage(res);
    }

    @GetMapping("/page")
    public ResponseMessage PageMatches(@RequestParam(value = "start", required = false) Date start,
                                       @RequestParam(value = "teamId", required = false) String teamId,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        if (start == null) {
            start = DateUtils.initDateByDay();
        } else {
            start = DateUtils.initDateByDay(start);
        }
        Page<MatchBasicRes> res = matchService.listMatches(start, teamId, pageNum, pageSize);

        return new ResponseMessage(res);
    }

    @GetMapping("/home/{size}")
    public ResponseMessage homeMatches(@PathVariable("size") Integer size, @RequestParam(value = "teamId", required = false) String teamId) {

        List<MatchBasicRes> res = matchService.homeMatches(size, teamId);
        return new ResponseMessage(res);
    }

    @GetMapping("/end/{size}")
    public ResponseMessage endMatches(@PathVariable("size") Integer size) {

        List<MatchBasicRes> res = matchService.endMatches(size);
        return new ResponseMessage(res);
    }

}
