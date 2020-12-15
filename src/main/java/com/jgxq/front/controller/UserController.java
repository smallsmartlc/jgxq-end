package com.jgxq.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.TalkHit;
import com.jgxq.common.res.TalkBasicRes;
import com.jgxq.common.res.UserActiveRes;
import com.jgxq.common.res.UserBasicRes;
import com.jgxq.common.res.UserFocusRes;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Focus;
import com.jgxq.front.entity.Talk;
import com.jgxq.front.entity.User;
import com.jgxq.front.mapper.FocusMapper;
import com.jgxq.front.service.FocusService;
import com.jgxq.front.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private FocusService focusService;

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
        focusQuery.select("target").eq("userkey", target);
        Page<Focus> page = new Page<>(pageNum, pageSize);
        focusService.page(page, focusQuery);

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
        focusQuery.select("userkey").eq("target", target);
        Page<Focus> page = new Page<>(pageNum, pageSize);
        focusService.page(page, focusQuery);

        Page<UserFocusRes> resPage = focusService.pageToResPage(page, userKey);
        return new ResponseMessage(resPage);
    }
}
