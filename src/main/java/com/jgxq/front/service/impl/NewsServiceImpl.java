package com.jgxq.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.NewsHit;
import com.jgxq.common.req.TagReq;
import com.jgxq.common.res.*;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.News;
import com.jgxq.front.mapper.NewsMapper;
import com.jgxq.front.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public Page<NewsBasicRes> pageNews(Integer pageNum, Integer pageSize) {
        Page<News> page = new Page<>(pageNum,pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id","title","cover").le("create_at",new Date(System.currentTimeMillis()))
                .orderByDesc("create_at");
        Page<News> newsPage = newsMapper.selectPage(page, wrapper);
        List<News> newsList = newsPage.getRecords();
        List<Integer> ids = newsList.stream().map(News::getId).collect(Collectors.toList());

//        Map<Integer, NewsHit> map = getHitsByIdList(ids);

        List<NewsBasicRes> newsBasicList = newsList.stream().map(news -> {
            NewsBasicRes newsBasicRes = new NewsBasicRes();
            BeanUtils.copyProperties(news, newsBasicRes);
//            newsBasicRes.setHit(map.get(newsBasicRes.getId()));
            return newsBasicRes;
        }).collect(Collectors.toList());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;

    }

    public NewsHit getHitById(Integer id) {
        return newsMapper.getHitById(id);
    }

    public Map<Integer,NewsHit> getHitsByIdList(List<Integer> ids) {
        return newsMapper.getHitByIds(ids);
    }

}
