package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.NewsHit;
import com.jgxq.common.req.TagReq;
import com.jgxq.common.res.*;
import com.jgxq.core.enums.RedisKeys;
import com.jgxq.front.define.ObjectType;
import com.jgxq.front.define.InteractionType;
import com.jgxq.front.entity.Collect;
import com.jgxq.front.entity.News;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.CollectMapper;
import com.jgxq.front.mapper.NewsMapper;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.sender.EsUtils;
import com.jgxq.front.sender.RedisCache;
import com.jgxq.front.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private EsUtils esClient;

    @Override
    public Page<NewsBasicRes> pageNews(Integer pageNum, Integer pageSize,Boolean topNews) {
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "cover").le("create_at", new Date(System.currentTimeMillis()))
                .orderByDesc("create_at");
        if(topNews){
            List<Integer> ids = new ArrayList<>();
            try {
                ids = redisCache.lrangeInt(RedisKeys.top_news.getKey());
            } catch (Exception e) {
                System.err.println("redis服务器异常");
            }
            if(!ids.isEmpty()){
                wrapper.notIn("id",ids);
            }
        }
        newsMapper.selectPage(page, wrapper);
        List<NewsBasicRes> newsBasicList = NewsListToBasicRes(page.getRecords());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;
    }

    @Override
    public Page<NewsBasicRes> pageAuthorNews(Integer pageNum, Integer pageSize, String userKey) {
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "cover").eq("author", userKey)
                .orderByDesc("create_at");
        newsMapper.selectPage(page, wrapper);
        List<NewsBasicRes> newsBasicList = NewsListToBasicRes(page.getRecords());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;
    }

    @Override
    public Page<NewsBasicRes> pageNewsByTag(Integer pageNum, Integer pageSize, Integer objectId, Integer objectType) {
        Page<News> page = new Page<>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.select("id", "title", "cover")
                .le("create_at", new Date(System.currentTimeMillis()))
                .gt("(select count(*) from tag where tag.news_id = news.id and tag.object_id = " + objectId + " and tag.object_type = " + objectType + ")", 0)
                .orderByDesc("create_at");
        newsMapper.selectPage(page, wrapper);
        List<NewsBasicRes> newsBasicList = NewsListToBasicRes(page.getRecords());
        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(newsBasicList);
        return resPage;
    }

    public Page<NewsBasicRes> pageNewsByTags(Integer pageNum, Integer pageSize, List<TagReq> tags) {
        Page<NewsBasicRes> page = new Page<>(pageNum, pageSize);
        if(tags == null || tags.isEmpty()){
            return page;
        }
        newsMapper.pageByTags(page, tags);
        return page;
    }

    public List<NewsBasicRes> NewsListToBasicRes(List<News> list) {
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
                "(select count(*) from comment where comment.object_id = news.id and comment.type = " + ObjectType.NEWS.getValue() + " and status = 1) as comments")
                .in("id", ids);

        List<Map<String, Object>> maps = newsMapper.selectMaps(newsQuery);
        Map<Integer, Integer> res = maps.stream().collect(Collectors.toMap(m -> Integer.parseInt(m.get("id").toString())
                , m -> Integer.parseInt(m.get("comments").toString())));
        return res;
    }

    public List<NewsBasicRes> listHomeNews(Integer size) {
        List<News> res = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        try {
            ids = redisCache.lrangeInt(RedisKeys.top_news.getKey());
        } catch (Exception e) {
            System.err.println("redis服务器异常");
        }
        if (!ids.isEmpty()) {
            QueryWrapper<News> newsQuery = new QueryWrapper<>();
            newsQuery.select("id", "title", "cover")
                    .in("id", ids)
                    .le("create_at", new Date(System.currentTimeMillis()))
                    .orderByDesc("create_at");
            if (size != null) {
                newsQuery.last("limit " + size);
            }
            res = newsMapper.selectList(newsQuery);
        }
        return NewsListToBasicRes(res);
    }

    public Page<NewsBasicRes> searchNewsPage(Integer pageNum, Integer pageSize, String keyword) {
        return searchNewsPageEs(pageNum, pageSize, keyword);//es
//        Page<News> page = new Page<>(pageNum, pageSize); //fixme sql
//        QueryWrapper<News> wrapper = new QueryWrapper<>();
//        wrapper.like("title", keyword).orderByAsc("LENGTH(title)");
//        newsMapper.selectPage(page, wrapper);
//        List<News> newsList = page.getRecords();
//        List<NewsBasicRes> newsBasicList = newsList.stream().map(news -> {
//            NewsBasicRes newsBasicRes = new NewsBasicRes();
//            BeanUtils.copyProperties(news, newsBasicRes);
//            return newsBasicRes;
//        }).collect(Collectors.toList());
//        Page<NewsBasicRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
//        resPage.setRecords(newsBasicList);
//        return resPage;
    }

    private Page<NewsBasicRes> searchNewsPageEs(Integer pageNum, Integer pageSize, String keyword) {
        SearchSourceBuilder builder = new SearchSourceBuilder();

        MatchQueryBuilder matchQuery1 = QueryBuilders.matchQuery("title", keyword);
        MatchQueryBuilder matchQuery2 = QueryBuilders.matchQuery("text", keyword);
        matchQuery1.boost(2);
        matchQuery2.boost(1);

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("status", 1);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(new BoolQueryBuilder().should(matchQuery1).should(matchQuery2));

        builder.query(boolQueryBuilder);
        Page<NewsBasicRes> resPage = esClient.search("jgxq_news", builder, NewsBasicRes.class, pageNum, pageSize);
        return resPage;
    }

}
