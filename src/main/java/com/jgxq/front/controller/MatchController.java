package com.jgxq.front.controller;


import com.alibaba.fastjson.JSON;
import com.jgxq.common.dto.ActionInfoReq;
import com.jgxq.common.dto.ActionReq;
import com.jgxq.common.req.MatchReq;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Match;
import com.jgxq.front.service.MatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
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

    @PostMapping
    public ResponseMessage addMatch(@RequestBody @Validated MatchReq matchReq){
        List<ActionReq> oldActionList = matchReq.getAction();
        List<ActionReq> actionList = matchReq.getAction();
        Map<String, List<ActionInfoReq>> map = oldActionList.stream()
                .collect(Collectors.toMap(a -> a.getTime(), a -> a.getInfoList()
                        , (o, n) -> {
                            o.addAll(n);
                            return o;
                        }));
        map.forEach((k, v)->actionList.add(new ActionReq(k,v)));
        actionList.stream().sorted(Comparator.comparing(ActionReq::getTime));
        String action = JSON.toJSONString(actionList);
        String matchInfo = JSON.toJSONString(matchReq.getMatchInfo());
        Match match = new Match();
        BeanUtils.copyProperties(matchReq, match);
        match.setAction(action);
        match.setMatchInfo(matchInfo);

        boolean flag = matchService.save(match);

        return new ResponseMessage(flag);
    }
}
