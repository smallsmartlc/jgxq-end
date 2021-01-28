package com.jgxq.front.mapper;

import com.jgxq.common.res.TagSearchRes;
import com.jgxq.front.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    @Select({
        "SELECT id as object_id , 1 as type,name ,head_image as logo from player WHERE name like concat('%',#{keyword},'%') and status = 1",
        "union",
        "Select id as object_id , 0 as type,name ,logo from team where name like concat('%',#{keyword},'%') and status = 1",
        "ORDER BY LENGTH(name) limit 10"
    })
    List<TagSearchRes> searchTag(@Param("keyword") String keyword);
}
