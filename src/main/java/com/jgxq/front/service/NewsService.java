package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.NewsBasicRes;
import com.jgxq.front.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
public interface NewsService extends IService<News> {
    Page<NewsBasicRes> pageNews(Integer pageNum, Integer pageSize,Boolean topNews);

    Page<NewsBasicRes> pageAuthorNews(Integer pageNum, Integer pageSize, String userKey);

    Page<NewsBasicRes> pageNewsByTag(Integer pageNum, Integer pageSize, Integer objectId,Integer objectType);
}
