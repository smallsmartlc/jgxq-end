package com.jgxq.front.controller;


import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.service.ThumbService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
@RestController
@RequestMapping("/thumb")
@UserPermissionConf
public class ThumbController {
    @Autowired
    private ThumbService thumbService;

    @PostMapping("/{type}/{id}")
    public ResponseMessage thumb(@PathVariable("type") Byte type,
                                 @PathVariable("id") Integer id,
                                 @RequestAttribute("userKey") String userkey) {

        Boolean flag = thumbService.thumb(type,id,userkey);

        return new ResponseMessage(flag);
    }
}
