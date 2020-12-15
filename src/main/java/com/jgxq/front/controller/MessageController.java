package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.MessageRes;
import com.jgxq.common.res.UserBasicRes;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.ReadType;
import com.jgxq.front.entity.Message;
import com.jgxq.front.service.MessageService;
import com.jgxq.front.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2020-12-15
 */
@RestController
@RequestMapping("/message")
@UserPermissionConf
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseMessage pageMessage(@RequestAttribute(value = "userKey") String userKey,
                                       @RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Message> messageQuery = new QueryWrapper<>();
        messageQuery.eq("target",userKey);
        messageService.page(page,messageQuery);
        List<Message> messageList = page.getRecords();
        Set<String> userKeys = messageList.stream()
                .map(Message::getUserkey).collect(Collectors.toSet());
        List<UserBasicRes> userBasicList = userService.getUserBasicByKeyList(userKeys);
        Map<String, UserBasicRes> map = userBasicList.stream()
                .collect(Collectors.toMap(UserBasicRes::getUserkey, u -> u));
        List<MessageRes> resList = messageList.stream().map(m -> {
            MessageRes res = new MessageRes();
            BeanUtils.copyProperties(m, res);
            res.setUser(map.get(m.getUserkey()));
            return res;
        }).collect(Collectors.toList());
        Page<MessageRes> resPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        resPage.setRecords(resList);
//        UpdateWrapper<Message> messageUpdate = new UpdateWrapper<>();
//        messageUpdate.set("read", ReadType.UNREAD.getValue());
        messageService.update().eq("target",userKey)
                .set("`read`", ReadType.READ.getValue()).update();
        return new ResponseMessage(resPage);
    }
}
