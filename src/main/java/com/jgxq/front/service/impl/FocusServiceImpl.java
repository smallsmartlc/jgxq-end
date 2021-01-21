package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.UserFocusRes;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Focus;
import com.jgxq.front.entity.User;
import com.jgxq.front.mapper.FocusMapper;
import com.jgxq.front.service.FocusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
@Service
public class FocusServiceImpl extends ServiceImpl<FocusMapper, Focus> implements FocusService {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public List<UserFocusRes> keyListToRes(List<String> keyList,String userKey) {
        //当前用户是否关注
        if(keyList.isEmpty()){
            return Collections.emptyList();
        }
        QueryWrapper<Focus> userFocusQuery = new QueryWrapper<>();
        userFocusQuery.select("target")
                .eq("userkey", userKey)
                .in("target", keyList);
        List<String> focusList = list(userFocusQuery)
                .stream().map(Focus::getTarget).collect(Collectors.toList());

        // 关注列表信息
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.in("userkey", keyList);
        List<User> users = userService.list(userQuery);

        List<UserFocusRes> resList = users.stream().map(u -> {
            UserFocusRes res = new UserFocusRes();
            BeanUtils.copyProperties(u, res);
            res.setFocus(focusList.contains(u.getUserkey()));
            return res;
        }).collect(Collectors.toList());
        return resList;
    }
}
