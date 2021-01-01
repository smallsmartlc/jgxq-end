package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.MatchBasicRes;
import com.jgxq.front.entity.Match;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-11
 */
public interface MatchService extends IService<Match> {

    Page<MatchBasicRes> listMatches(Date start, String teamId, Integer pageNum, Integer pageSize);

    List<MatchBasicRes> homeMatches(Integer size, String teamId);

    List<MatchBasicRes> endMatches(Integer size);
}
