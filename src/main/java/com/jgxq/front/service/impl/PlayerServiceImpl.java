package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.res.PlayerBasicRes;
import com.jgxq.common.res.TeamBasicRes;
import com.jgxq.front.entity.Player;
import com.jgxq.front.mapper.PlayerMapper;
import com.jgxq.front.service.PlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-10
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {

    @Autowired
    private PlayerMapper playerMapper;

    public List<PlayerBasicRes> geyBasicByIds(Collection<Integer> ids){
        QueryWrapper<Player> wrapper = new QueryWrapper<>();
        wrapper.select("id","name","head_image")
                .in("id",ids);
        List<Player> players = playerMapper.selectList(wrapper);
        List<PlayerBasicRes> playRes = players.stream().map(p -> {
            PlayerBasicRes res = new PlayerBasicRes();
            BeanUtils.copyProperties(p, res);
            return res;
        }).collect(Collectors.toList());
        return playRes;
    }
}
