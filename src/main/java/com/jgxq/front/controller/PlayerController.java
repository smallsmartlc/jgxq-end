package com.jgxq.front.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.dto.PlayerInfo;
import com.jgxq.common.dto.PlayerTeam;
import com.jgxq.common.req.PlayerReq;
import com.jgxq.common.res.PlayerRes;
import com.jgxq.common.res.PlayerTeamRes;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.PositionEnum;
import com.jgxq.front.define.StrongFootEnum;
import com.jgxq.front.entity.Player;
import com.jgxq.front.entity.Team;
import com.jgxq.front.service.PlayerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseMessage addPlayer(@RequestBody PlayerReq playerReq) {
        String infos = JSON.toJSONString(playerReq.getInfos());

        Player player = new Player();
        player.setInfos(infos);
        BeanUtils.copyProperties(playerReq, player);

        playerService.save(player);

        return new ResponseMessage(player.getId());
    }

    @PutMapping("{id}")
    public ResponseMessage updatePlayer(@PathVariable("id") Integer id,
                                        @RequestBody PlayerReq playerReq) {
        String infos = JSON.toJSONString(playerReq.getInfos());

        Player player = new Player();
        BeanUtils.copyProperties(playerReq, player);
        player.setInfos(infos);
        player.setId(id);

        boolean flag = playerService.updateById(player);

        return new ResponseMessage(flag);
    }

    @DeleteMapping("{id}")
    public ResponseMessage deletePlayer(@PathVariable("id") Integer id) {
        boolean flag = playerService.removeById(id);
        return new ResponseMessage(flag);
    }

    @GetMapping("{id}")
    public ResponseMessage getPlayerById(@PathVariable("id") Integer id) {
        Player player = playerService.getById(id);
        if (player == null) {
            return new ResponseMessage(player);
        }
        PlayerRes playerRes = new PlayerRes();
        BeanUtils.copyProperties(player, playerRes);
        playerRes.setStrongFoot(StrongFootEnum.getFootByVal(player.getStrongFoot()));
        playerRes.setPosition(PositionEnum.getPositionByVal(player.getPosition()));
        List<PlayerInfo> infos = JSON.parseArray(player.getInfos(), PlayerInfo.class);
        playerRes.setInfos(infos);
        return new ResponseMessage(playerRes);
    }

    @GetMapping("team/{teamId}")
    public ResponseMessage getTeamMembers(@PathVariable("teamId") Integer teamId) {
        QueryWrapper<Player> playQuery = new QueryWrapper<>();
        playQuery.select("id","name","head_image","nation","number","position","birthday")
                .eq("team",teamId);
        List<Player> list = playerService.list(playQuery);

        List<PlayerTeamRes> res = new ArrayList<>();
        list.stream().map(player -> {
            //转为PlayerTeamList
            PlayerTeam playerTeam = new PlayerTeam();
            BeanUtils.copyProperties(player, playerTeam);
            playerTeam.setPosition(player.getPosition().intValue());
            return playerTeam;
        }).collect(Collectors.groupingBy((playerTeam -> PositionEnum.getPositionByVal(playerTeam.getPosition()))))
                //转为map
                .forEach((key, value) -> {
                    //遍历map
                    res.add(new PlayerTeamRes(key, value));
                });

        return new ResponseMessage(res);

    }

}
