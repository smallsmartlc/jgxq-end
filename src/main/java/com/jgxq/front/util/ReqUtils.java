package com.jgxq.front.util;

import com.alibaba.fastjson.JSON;
import com.jgxq.common.dto.ActionInfo;
import com.jgxq.common.dto.Action;
import com.jgxq.common.req.MatchReq;
import com.jgxq.common.req.TeamReq;
import com.jgxq.front.entity.Match;
import com.jgxq.front.entity.Team;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
public class ReqUtils {
    public static Match matchReqToMatch(MatchReq matchReq){
        List<Action> oldActionList = matchReq.getAction();
        List<Action> actionList = new ArrayList<>();
        Map<String, List<ActionInfo>> map = oldActionList.stream()
                .collect(Collectors.toMap(a -> a.getTime(), a -> a.getInfoList()
                        , (o, n) -> {
                            o.addAll(n);
                            return o;
                        }));
        List<Action> finalActionList = actionList;
        map.forEach((k, v)-> finalActionList.add(new Action(k,v)));
        actionList = actionList.stream().sorted(Comparator.comparing(Action::getTime)).collect(Collectors.toList());
        String action = JSON.toJSONString(actionList);
        String matchInfo = JSON.toJSONString(matchReq.getMatchInfo());
        Match match = new Match();
        BeanUtils.copyProperties(matchReq, match);
        match.setAction(action);
        match.setMatchInfo(matchInfo);

        return match;
    }

    public static Team teamReqToTeam(TeamReq teamReq){
        String info = JSON.toJSONString(teamReq.getInfos());
        Team team = new Team();
        BeanUtils.copyProperties(teamReq, team);
        team.setInfos(info);
        return team;
    }

}
