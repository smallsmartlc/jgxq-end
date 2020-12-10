package com.jgxq.front.service.impl;

import com.jgxq.front.entity.Player;
import com.jgxq.front.mapper.PlayerMapper;
import com.jgxq.front.service.PlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
