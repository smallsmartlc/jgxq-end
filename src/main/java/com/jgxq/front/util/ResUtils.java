package com.jgxq.front.util;

import com.alibaba.fastjson.JSON;
import com.jgxq.common.dto.Action;
import com.jgxq.common.dto.ActionInfo;
import com.jgxq.common.dto.MatchInfo;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.common.res.MatchRes;
import com.jgxq.front.define.MatchActionType;
import com.jgxq.front.entity.Match;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author LuCong
 * @since 2020-12-11
 **/
@Component
public class ResUtils {

    public static MatchRes matchToMatchRes(Match match) {

        List<Action> actions = JSON.parseArray(match.getAction(), Action.class);
        MatchInfo matchInfo = JSON.parseObject(match.getMatchInfo(), MatchInfo.class);
        MatchRes matchRes = new MatchRes();
        BeanUtils.copyProperties(match, matchRes);
        matchRes.setMatchInfo(matchInfo);
        matchRes.setActions(actions);
        return matchRes;
    }
}
