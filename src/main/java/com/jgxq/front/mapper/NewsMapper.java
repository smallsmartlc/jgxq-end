package com.jgxq.front.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.HitBatchDto;
import com.jgxq.common.dto.NewsHit;
import com.jgxq.common.req.TagReq;
import com.jgxq.common.res.NewsBasicRes;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {

    @Select({
            "select (SELECT count(*) from `comment` where type = 0 and object_id = news.id) as comments,",
            "(SELECT count(*) from thumb where type = 0 and object_id = news.id) as thumbs,",
            "(SELECT count(*) from thumb where type = 0 and object_id = news.id) as collects",
            "from news where id = #{id}"
    })
    NewsHit getHitById(Integer id);

    @Select({
            "<script>",
            "SELECT DISTINCT(news.id) , news.title , news.cover FROM news inner join tag on tag.news_id = news.id",
            "where (tag.object_id,tag.object_type) in ",
            "<foreach collection='list' open='(' close=')' separator=',' item='item'>",
            "   (#{item.objectId},#{item.type})",
            "</foreach>",
            "and news.status = 1 order by news.create_at desc",
            "</script>"
    })
    Page<NewsBasicRes> pageByTags(Page<NewsBasicRes> page, @Param("list")List<TagReq> tags);
}
