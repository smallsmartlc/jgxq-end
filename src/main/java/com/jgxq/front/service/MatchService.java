package com.jgxq.front.service;

import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.front.entity.Match;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-11
 */
public interface MatchService extends IService<Match> {

    List<MatchBasicRes> listMatches(Integer size, Date start);

    List<MatchBasicRes> homeMatches(Integer size);
}
