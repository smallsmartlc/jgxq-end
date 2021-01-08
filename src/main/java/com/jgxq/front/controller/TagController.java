package com.jgxq.front.controller;


import com.jgxq.common.res.TagSearchRes;
import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-12
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("search")
    public ResponseMessage searchTag(@RequestParam("keyword") String keyword){
        List<TagSearchRes> list = tagService.searchTag(keyword);
        return new ResponseMessage(list);
    }
}
