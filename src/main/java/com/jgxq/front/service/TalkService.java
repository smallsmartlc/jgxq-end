package com.jgxq.front.service;

import com.jgxq.common.dto.TalkHit;
import com.jgxq.front.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-14
 */
public interface TalkService extends IService<Talk> {

    TalkHit getHit(Integer id,String userKey);

    Map<Integer, TalkHit> getHit(Collection<Integer> ids, String userKey);
}
