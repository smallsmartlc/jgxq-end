package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Focus;
import com.jgxq.front.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
@RestController
@RequestMapping("/focus")
@UserPermissionConf
public class FocusController {

    @Autowired
    private FocusService focusService;

    //关注/取关
    @PutMapping
    public ResponseMessage focus(@RequestParam("target") String target,
                                 @RequestAttribute(value = "userKey") String userKey,
                                 @RequestParam("focused") Boolean focused) {
        if(target==userKey){
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"不能关注本人");
        }
        boolean flag = false;
        if (focused) {
            QueryWrapper<Focus> focusQuery = new QueryWrapper<>();
            focusQuery.eq("userkey", userKey)
                    .eq("target", target).orderByDesc("id");
            flag = focusService.remove(focusQuery);
        } else {
            Focus focus = new Focus();
            focus.setUserkey(userKey);
            focus.setTarget(target);
            flag = focusService.save(focus);
        }
        return new ResponseMessage(flag);
    }
}
