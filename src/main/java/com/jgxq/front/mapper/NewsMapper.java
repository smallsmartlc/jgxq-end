package com.jgxq.front.mapper;

import com.jgxq.common.dto.NewsHit;
import com.jgxq.front.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    NewsHit getHitById(Integer id);

    Map<Integer, NewsHit> getHitByIds(List<Integer> ids);
}
