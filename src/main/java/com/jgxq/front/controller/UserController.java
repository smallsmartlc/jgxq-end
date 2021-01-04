package com.jgxq.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.CollectNewsRes;
import com.jgxq.common.res.NewsBasicRes;
import com.jgxq.common.res.UserActiveRes;
import com.jgxq.common.res.UserFocusRes;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Collect;
import com.jgxq.front.entity.Focus;
import com.jgxq.front.service.impl.CollectServiceImpl;
import com.jgxq.front.service.impl.FocusServiceImpl;
import com.jgxq.front.service.impl.NewsServiceImpl;
import com.jgxq.front.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LuCong
 * @since 2020-12-08
 **/
@RestController
@RequestMapping("/user")
@UserPermissionConf
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FocusServiceImpl focusService;

    @Autowired
    private CollectServiceImpl collectService;

    @Autowired
    private NewsServiceImpl newsService;

    @GetMapping("info")
    public ResponseMessage getUserInfo(@RequestParam(value = "target", required = false) String target,
                                       @RequestAttribute(value = "userKey") String userKey) {
        if (target == null) {
            target = userKey;
        }

        UserActiveRes res = userService.getUserActiveRes(target);

        return new ResponseMessage(res);
    }

    @GetMapping("page/focus")
    public ResponseMessage getFocusUser(@RequestParam("pageNum") Integer pageNum,
                                        @RequestParam("pageSize") Integer pageSize,
                                        @RequestParam(value = "target", required = false) String target,
                                        @RequestAttribute(value = "userKey") String userKey) {
        if (target == null) {
            target = userKey;
        }
        QueryWrapper<Focus> focusQuery = new QueryWrapper<>();
        focusQuery.select("target").eq("userkey", target).orderByDesc("id");
        Page<Focus> page = new Page<>(pageNum, pageSize);
        focusService.page(page, focusQuery);
        if(page.getRecords().isEmpty()){
            new ResponseMessage(page);
        }
        Page<UserFocusRes> resPage = focusService.pageToResPage(page, userKey);
        return new ResponseMessage(resPage);
    }

    @GetMapping("page/fans")
    public ResponseMessage getFansUser(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize,
                                       @RequestParam(value = "target", required = false) String target,
                                       @RequestAttribute(value = "userKey") String userKey) {
        if (target == null) {
            target = userKey;
        }
        QueryWrapper<Focus> focusQuery = new QueryWrapper<>();
        focusQuery.select("userkey").eq("target", target).orderByDesc("id");
        Page<Focus> page = new Page<>(pageNum, pageSize);
        focusService.page(page, focusQuery);
        if(page.getRecords().isEmpty()){
            new ResponseMessage(page);
        }
        Page<UserFocusRes> resPage = focusService.pageToResPage(page, userKey);
        return new ResponseMessage(resPage);
    }

    @GetMapping("page/collect")
    public ResponseMessage getCollectNews(@RequestParam("pageNum") Integer pageNum,
                                          @RequestParam("pageSize") Integer pageSize,
                                          @RequestAttribute(value = "userKey") String userKey){
        QueryWrapper<Collect> collectQuery = new QueryWrapper<>();
        collectQuery.eq("userkey",userKey).orderByDesc("create_time");
        Page<Collect> page = new Page<>(pageNum,pageSize);
        collectService.page(page,collectQuery);
        List<Integer> newsIds = page.getRecords().stream().map(Collect::getObjId).collect(Collectors.toList());
        if(newsIds.isEmpty()){
            return new ResponseMessage(page);
        }
        List<NewsBasicRes> newsBasicResList = newsService.NewsListToBasicRes(newsService.listByIds(newsIds));
        Map<Integer, NewsBasicRes> map = newsBasicResList.stream().collect(Collectors.toMap(NewsBasicRes::getId, Function.identity()));
        List<CollectNewsRes> records = page.getRecords().stream().map(c -> {
            CollectNewsRes temp = new CollectNewsRes();
            BeanUtils.copyProperties(c, temp);
            temp.setNews(map.get(c.getObjId()));
            return temp;
        }).collect(Collectors.toList());
        Page<CollectNewsRes> resPage = new Page<>(pageNum, pageSize);
        resPage.setRecords(records);
        return new ResponseMessage(resPage);
    }
}
