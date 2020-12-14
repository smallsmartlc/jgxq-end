package com.jgxq.front.controller;

import com.jgxq.core.anotation.AllowAccess;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
@RestController
@UserPermissionConf
public class TestController {

    @RequestMapping("/test")
    @AllowAccess
    public ResponseMessage getSomething(@RequestAttribute(value = "userKey",required = false)String userKey){

        return new ResponseMessage("hhhhh");
    }

    @AllowAccess
    @RequestMapping("/test2")
    public ResponseMessage getSomething2(){

        return new ResponseMessage("hhhhh");
    }

}
