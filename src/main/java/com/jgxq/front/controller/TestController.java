package com.jgxq.front.controller;

import com.jgxq.core.resp.ResponseMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
@RestController
public class TestController {

    @RequestMapping("/test")
    public ResponseMessage getSomething(){

        throw new NullPointerException();
    }

}