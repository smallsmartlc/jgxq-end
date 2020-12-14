package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.TalkHit;
import com.jgxq.common.req.NewsReq;
import com.jgxq.common.res.TalkBasicRes;
import com.jgxq.common.res.TalkRes;
import com.jgxq.common.res.UserBasicRes;
import com.jgxq.common.res.UserLoginRes;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.News;
import com.jgxq.front.entity.Tag;
import com.jgxq.front.entity.Talk;
import com.jgxq.front.entity.User;
import com.jgxq.front.service.TalkService;
import com.jgxq.front.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-14
 */
@RestController
@RequestMapping("/talk")
@UserPermissionConf
public class TalkController {

    @Autowired
    private TalkService talkService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    public ResponseMessage addTalk(@RequestParam String text,
                                   @RequestAttribute("userKey") String userKey) {
        Talk talk = new Talk();
        talk.setAuthor(userKey);
        talk.setText(text);
        talkService.save(talk);
        return new ResponseMessage(talk.getId());
    }

    @DeleteMapping("{id}")
    public ResponseMessage deleteTalk(@PathVariable("id") Integer id,
                                      @RequestAttribute("userKey") String userKey) {
        QueryWrapper<Talk> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id)
                .eq("author", userKey);
        boolean flag = talkService.remove(wrapper);

        return new ResponseMessage(flag);
    }

    @GetMapping("{id}")
    public ResponseMessage getTalk(@PathVariable Integer id,
                                   @RequestAttribute("userKey") String userKey) {

        Talk talk = talkService.getById(id);
        User user = userService.getUserByPK("userkey", talk.getAuthor());
        UserLoginRes userRes = userService.userToLoginRes(user);
        TalkRes talkRes = new TalkRes();
        BeanUtils.copyProperties(talk, talkRes);
        talkRes.setAuthor(userRes);
        TalkHit hit = talkService.getHit(talk.getId(), userKey);
        talkRes.setHit(hit);
        return new ResponseMessage(talkRes);
    }

    @GetMapping("/page")
    @AllowAccess
    public ResponseMessage pageTalk(@RequestParam("pageNum") Integer pageNum,
                                    @RequestParam("pageSize") Integer pageSize,
                                    @RequestAttribute(value = "userKey" ,required = false) String userKey){
        Page<Talk> talkPage = new Page<>(pageNum, pageSize);

        QueryWrapper<Talk> talkQuery = new QueryWrapper<>();
        talkQuery.orderByDesc("id");

        talkService.page(talkPage, talkQuery);

        List<Talk> records = talkPage.getRecords();
        List<Integer> ids = records.stream().map(Talk::getId).collect(Collectors.toList());
        Set<String> userKeys = records.stream().map(Talk::getAuthor).collect(Collectors.toSet());
        List<UserBasicRes> userBasicList = userService.getUserBasicByKeyList(userKeys);
        Map<String, UserBasicRes> map = userBasicList.stream().collect(Collectors.toMap(UserBasicRes::getUserkey, u -> u));
        Map<Integer, TalkHit> hitMap = talkService.getHit(ids, userKey);
        List<TalkBasicRes> resList = records.stream().map(t -> {
            TalkBasicRes talkBasicRes = new TalkBasicRes();
            BeanUtils.copyProperties(t, talkBasicRes);
            talkBasicRes.setHit(hitMap.get(t.getId()));
            talkBasicRes.setAuthor(map.get(t.getAuthor()));
            return talkBasicRes;
        }).collect(Collectors.toList());

        return new ResponseMessage(resList);
    }

}
