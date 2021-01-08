package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.NewsHit;
import com.jgxq.common.req.NewsReq;
import com.jgxq.common.res.*;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.TagType;
import com.jgxq.front.entity.News;
import com.jgxq.front.entity.Tag;
import com.jgxq.front.service.TagService;
import com.jgxq.front.service.impl.NewsServiceImpl;
import com.jgxq.front.service.impl.TeamServiceImpl;
import com.jgxq.front.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private TagService tagService;

    @PostMapping
    public ResponseMessage addNews(@RequestBody @Validated NewsReq newsReq,
                                   @RequestAttribute("userKey") String userKey) {
        News news = new News();
        BeanUtils.copyProperties(newsReq, news);
        news.setAuthor(userKey);
        boolean flag = newsService.save(news);
        if(flag){
            List<Tag> tagList = newsReq.getTags().stream().map(t -> {
                Tag tag = new Tag();
                BeanUtils.copyProperties(t, tag);
                tag.setNewsId(news.getId());
                tag.setObjectType(t.getType().byteValue());
                return tag;
            }).collect(Collectors.toList());
            tagService.saveBatch(tagList);
        }
        return new ResponseMessage(news.getId());
    }

    @PutMapping("{id}")
    @Transactional
    public ResponseMessage updateNews(@PathVariable("id") Integer id,
                                      @RequestBody @Validated NewsReq newsReq,
                                      @RequestAttribute("userKey") String userKey) {
        News news = new News();
        BeanUtils.copyProperties(newsReq, news);
        UpdateWrapper<News> newsUpdate = new UpdateWrapper<>();
        newsUpdate.eq("id",id).eq("author",userKey);
        boolean flag = newsService.update(news,newsUpdate);
        if(flag){
            QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
            tagQuery.eq("news_id", id);
            tagService.remove(tagQuery);
            List<Tag> tagList = newsReq.getTags().stream().map(t -> {
                Tag tag = new Tag();
                BeanUtils.copyProperties(t, tag);
                tag.setObjectType(t.getType().byteValue());
                tag.setNewsId(id);
                return tag;
            }).collect(Collectors.toList());
            tagService.saveBatch(tagList);
        }
        return new ResponseMessage(flag);
    }

    @DeleteMapping("{id}")
    public ResponseMessage deleteNews(@PathVariable("id") Integer id,
                                      @RequestAttribute("userKey") String userKey) {
        UpdateWrapper<News> newsUpdate = new UpdateWrapper<>();
        newsUpdate.eq("id",id).eq("author",userKey);
        boolean flag = newsService.remove(newsUpdate);
        QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
        tagQuery.eq("news_id", id);
        tagService.remove(tagQuery);
        return new ResponseMessage(flag);
    }

    @GetMapping("{id}")
    @AllowAccess
    public ResponseMessage getNews(@PathVariable("id") Integer id,
                                   @RequestAttribute(value = "userKey", required = false) String userKey) {
        QueryWrapper<News> newsQuery = new QueryWrapper<>();
        newsQuery.eq("id", id)
                .le("create_at", new Date(System.currentTimeMillis()));
        News news = newsService.getOne(newsQuery);
        if (news == null) {
            return new ResponseMessage(CommonErrorCode.BAD_PARAMETERS.getErrorCode(), "没有该记录");
        }
        AuthorRes authorInfo = userService.getAuthorInfo(news.getAuthor());
        List<TagSearchRes> tagList = tagService.getTagList(id);
        NewsRes newsRes = new NewsRes();
        BeanUtils.copyProperties(news, newsRes);
        newsRes.setAuthor(authorInfo);
        newsRes.setTags(tagList);

        NewsHit hit = newsService.getHitById(news.getId(), userKey);
        newsRes.setHit(hit);

        return new ResponseMessage(newsRes);
    }


    @GetMapping("page/{pageNum}/{pageSize}")
    @AllowAccess
    public ResponseMessage pageNews(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize) {
        Page<NewsBasicRes> list = newsService.pageNews(pageNum, pageSize);
        return new ResponseMessage(list);
    }

    @GetMapping("page/author/{pageNum}/{pageSize}")
    public ResponseMessage pageAuthorNews(@PathVariable("pageNum") Integer pageNum,
                                          @PathVariable("pageSize") Integer pageSize,
                                          @RequestAttribute("userKey") String userKey) {
        Page<NewsBasicRes> list = newsService.pageAuthorNews(pageNum, pageSize,userKey);
        return new ResponseMessage(list);
    }

    @GetMapping("page/tag/{pageNum}/{pageSize}")
    @AllowAccess
    public ResponseMessage pageNews(@PathVariable("pageNum") Integer pageNum,
                                    @PathVariable("pageSize") Integer pageSize,
                                    @RequestParam("objectId") Integer objectId,
                                    @RequestParam("type") Integer objectType) {

        Page<NewsBasicRes> list = newsService.pageNewsByTag(pageNum, pageSize, objectId, objectType);
        return new ResponseMessage(list);
    }

    @GetMapping("home/top")
    @AllowAccess
    public ResponseMessage homeNews(@RequestParam(value = "size", required = false) Integer size) {
        if (size != null && (size < 1 || size > 30)) return new ResponseMessage(null);
        List<NewsBasicRes> list = newsService.listHomeNews(size);
        return new ResponseMessage(list);
    }

}
