package com.jgxq.front.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.TagSearchRes;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("page/{pageNum}/{pageSize}")
    public ResponseMessage pageTag(@PathVariable("pageNum") Integer pageNum,
                                   @PathVariable("pageSize") Integer pageSize,
                                   @RequestParam("keyword") String keyword){
        Page<TagSearchRes> list = tagService.pageTag(pageNum,pageSize,keyword);
        return new ResponseMessage(list);
    }

    @GetMapping("search")
    public ResponseMessage searchTag(@RequestParam("keyword") String keyword){
        List<TagSearchRes> list = tagService.searchTag(keyword);
        return new ResponseMessage(list);
    }

    @GetMapping("recommend")
    public ResponseMessage searchRecommend(@RequestParam("keyword") String keyword){
        List<String> res = tagService.searchTag(keyword)
                .stream().map(TagSearchRes::getName).collect(Collectors.toList());
        return new ResponseMessage(res);
    }
}
