package com.jgxq.front.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.dto.TagInfo;
import com.jgxq.common.req.NewsReq;
import com.jgxq.common.res.AuthorRes;
import com.jgxq.common.res.NewsRes;
import com.jgxq.common.res.TagRes;
import com.jgxq.common.utils.DateUtils;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.ForumErrorCode;
import com.jgxq.front.entity.News;
import com.jgxq.front.service.NewsService;
import com.jgxq.front.service.impl.NewsServiceImpl;
import com.jgxq.front.service.impl.TeamServiceImpl;
import com.jgxq.front.service.impl.UserServiceImpl;
import com.jgxq.front.util.ResUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@RestController
@RequestMapping("/news")
@UserPermissionConf
public class NewsController {

    @Autowired
    private NewsServiceImpl newsService;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ResponseMessage addNews(@RequestBody @Validated NewsReq newsReq,
                                   @RequestAttribute("userKey") String userKey) {
        String tags = JSON.toJSONString(newsReq.getTags());
        News news = new News();
        BeanUtils.copyProperties(newsReq, news);
        news.setTag(tags);
        news.setAuthor(userKey);
        newsService.save(news);
        return new ResponseMessage(news.getId());
    }

    @PutMapping("{id}")
    private ResponseMessage updateNews(@PathVariable("id") Integer id,
                                       @RequestBody @Validated NewsReq newsReq) {
        String tags = JSON.toJSONString(newsReq.getTags());
        News news = new News();
        BeanUtils.copyProperties(newsReq, news);
        news.setTag(tags);
        news.setId(id);
        boolean flag = newsService.updateById(news);
        return new ResponseMessage(flag);
    }

    @DeleteMapping("{id}")
    private ResponseMessage updateNews(@PathVariable("id") Integer id) {
        boolean flag = newsService.removeById(id);
        return new ResponseMessage(flag);
    }

    @GetMapping("{id}")
    private ResponseMessage getNews(@PathVariable("id") Integer id) {
        QueryWrapper<News> newsQuery = new QueryWrapper<>();
        newsQuery.eq("id",id)
                .le("create_at", new Date(System.currentTimeMillis()));
        News news = newsService.getOne(newsQuery);
        if(news == null){
            return new ResponseMessage(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"没有该记录");
        }
        AuthorRes authorInfo = userService.getAuthorInfo(news.getAuthor());
        TagRes tagRes = newsService.tagInfosToRes(news.getTag());
        NewsRes newsRes = new NewsRes();
        BeanUtils.copyProperties(news,newsRes);
        newsRes.setAuthor(authorInfo);
        newsRes.setTag(tagRes);

        return new ResponseMessage(newsRes);
    }


}
