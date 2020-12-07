package com.jgxq.front.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.req.UserLoginReq;
import com.jgxq.common.req.UserRegReq;
import com.jgxq.common.res.UserLoginRes;
import com.jgxq.common.res.UserRegRes;
import com.jgxq.common.utils.CookieUtils;
import com.jgxq.common.utils.JwtUtil;
import com.jgxq.common.utils.LoginUtils;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.ForumErrorCode;
import com.jgxq.front.entity.User;
import com.jgxq.front.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${JWTParam.cookieSecure}")
    private boolean cookieSecure;

    @Autowired
    private UserService userService;


    @PostMapping("login")
    public ResponseMessage login(@RequestBody @Validated UserLoginReq userReq,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

        String email = userReq.getEmail();
        String password = userReq.getPassword();
        User user = userService.login(email, password);

        if(user==null){
            //登陆失败
            return new ResponseMessage(ForumErrorCode.TelOrPassword_Error.getErrorCode(),"手机号或密码错误");
        }
        //登陆成功,生成token
        String token = LoginUtils.generateToken(user.getEmail(),user.getUserkey());

        Cookie cookie = new Cookie(JwtUtil.JG_COOKIE,token);
        cookie.setMaxAge((int) (CookieUtils.TOKEN_EXP/1000));
        if (cookieSecure) {
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
        }
        String host = request.getServerName();
        if(!CookieUtils.LOCALHOST.equals(host)){
            cookie.setDomain(host.substring(host.indexOf(".") + 1));
        }
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Set-Cookie", response.getHeader("Set-Cookie") + "; SameSite=Lax");
        UserLoginRes userRes = new UserLoginRes();
        BeanUtils.copyProperties(user, userRes);

        return new ResponseMessage(userRes);
    }


    @PostMapping("register")
    public ResponseMessage register(@RequestBody @Validated UserRegReq userReq){

        //TODO 验证码

        if (!LoginUtils.checkPassword(userReq.getPassword())) {
            // 判断密码规则是否合法，字母、数字、特殊字符最少2种组合（不能有中文和空格）
            return new ResponseMessage(CommonErrorCode.BAD_PARAMETERS.getErrorCode(), "密码必须含有字母,数字,特殊字符最少两种组合!");
        }

        int count = userService.count(new QueryWrapper<User>().eq("email", userReq.getEmail()));
        if(count > 0){
            return new ResponseMessage(ForumErrorCode.User_Exists.getErrorCode(), "用户已存在");
        }

        UserRegRes userRes = userService.addUser(userReq);

        if(userRes == null){
            return new ResponseMessage(CommonErrorCode.UNKNOWN_ERROR.getErrorCode(),"注册失败");
        }

        return new ResponseMessage(userRes);
    }

}
