package com.jgxq.front.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.dto.TalkHit;
import com.jgxq.common.res.TalkBasicRes;
import com.jgxq.common.res.UserActiveRes;
import com.jgxq.common.res.UserBasicRes;
import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Talk;
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

    @GetMapping("info")
    public ResponseMessage getUserInfo(@RequestParam(value = "target", required = false) String target,
                                       @RequestAttribute(value = "userKey") String userKey) {
        if (target == null) {
            target = userKey;
        }

        UserActiveRes res = userService.getUserActiveRes(target);

        return new ResponseMessage(res);
    }

}
