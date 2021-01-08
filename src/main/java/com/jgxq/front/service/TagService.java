package com.jgxq.front.service;

import com.jgxq.common.res.TagRes;
import com.jgxq.common.res.TagSearchRes;
import com.jgxq.front.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
public interface TagService extends IService<Tag> {
    TagRes getTags(Integer newsId);

    List<TagSearchRes> getTagList(Integer newsId);

    List<TagSearchRes> searchTag(String keyword);
}
