package com.jgxq.front.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.front.entity.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-09
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

    IPage pageTeamsByHeat(Page<Team> page);
}
