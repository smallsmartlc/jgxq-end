package com.jgxq.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.NewsHit;
import com.jgxq.common.req.TagReq;
import com.jgxq.common.res.*;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.Collect;
import com.jgxq.front.entity.Comment;
import com.jgxq.front.entity.News;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.CollectMapper;
import com.jgxq.front.mapper.CommentMapper;
import com.jgxq.front.mapper.NewsMapper;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.corba.se.spi.ior.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private ThumbMapper thumbMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public Page<NewsBasicRes> pageNews(Integer pageNum, Integer pageSize) {
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "cover").le("create_at", new Date(System.currentTimeMillis()))
                .orderByDesc("create_at");
        newsMapper.selectPage(page, wrapper);
        List<NewsBasicRes> newsBasicList = NewsListToBasicRes(page.getRecords());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;
    }

    @Override
    public Page<NewsBasicRes> pageNewsByTag(Integer pageNum, Integer pageSize, Integer objectId,Integer objectType){
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "cover")
                .le("create_at", new Date(System.currentTimeMillis()))
                .gt("(select count(*) from tag where tag.news_id = news.id and tag.object_id = "+ objectId +" and tag.object_type = "+objectType+")",0)
                .orderByDesc("create_at");
        newsMapper.selectPage(page, wrapper);
        List<NewsBasicRes> newsBasicList = NewsListToBasicRes(page.getRecords());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;
    }

    private List<NewsBasicRes> NewsListToBasicRes(List<News> list){
        List<Integer> ids = list.stream().map(News::getId).collect(Collectors.toList());

        Map<Integer, Integer> map = getCommentNumByIdList(ids);

        List<NewsBasicRes> newsBasicList = list.stream().map(news -> {
            NewsBasicRes newsBasicRes = new NewsBasicRes();
            BeanUtils.copyProperties(news, newsBasicRes);
            newsBasicRes.setComments(map.get(news.getId()));
            return newsBasicRes;
        }).collect(Collectors.toList());

        return newsBasicList;
    }

    public NewsHit getHitById(Integer id, String userKey) {
        NewsHit hit = newsMapper.getHitById(id);
        if (userKey != null) {
            QueryWrapper<Thumb> thumbQuery = new QueryWrapper<>();
            QueryWrapper<Collect> collectQuery = new QueryWrapper<>();
            thumbQuery.eq("userkey", userKey)
                    .eq("type", InteractionType.NEWS.getValue())
                    .eq("object_id", id);

            collectQuery.eq("userkey", userKey)
                    .eq("type", InteractionType.NEWS.getValue())
                    .eq("obj_id", id);
            boolean thumb = thumbMapper.selectCount(thumbQuery) > 0;
            boolean collect = collectMapper.selectCount(collectQuery) > 0;
            hit.setThumb(thumb);
            hit.setCollect(collect);
        }
        return hit;
    }

    public Map<Integer, Integer> getCommentNumByIdList(List<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        QueryWrapper<News> newsQuery = new QueryWrapper<>();
        newsQuery.select("id",
                "(select count(*) from comment where comment.object_id = news.id) as comments")
                .in("id", ids);

        List<Map<String, Object>> maps = newsMapper.selectMaps(newsQuery);
        Map<Integer, Integer> res = maps.stream().collect(Collectors.toMap(m -> Integer.parseInt(m.get("id").toString())
                , m -> Integer.parseInt(m.get("comments").toString())));
        return res;
    }

}
