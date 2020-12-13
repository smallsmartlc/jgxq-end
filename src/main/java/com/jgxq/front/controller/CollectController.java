package com.jgxq.front.controller;


import com.jgxq.core.anotation.UserPermissionConf;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.service.CollectService;
import com.jgxq.front.service.ThumbService;
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
@RequestMapping("/collect")
@UserPermissionConf
public class CollectController {

    @Autowired
    private CollectService collectService;

    @PostMapping("/{type}/{id}")
    public ResponseMessage collect(@PathVariable("type") Byte type,
                                   @PathVariable("id") Integer id,
                                   @RequestAttribute("userKey") String userkey,
                                   @RequestParam("collected") boolean collected) {

        Boolean flag = collectService.collect(type, id, userkey,collected);

        return new ResponseMessage(flag);
    }

}
