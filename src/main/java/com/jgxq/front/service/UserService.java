package com.jgxq.front.service;

import com.jgxq.common.req.UserRegReq;
import com.jgxq.common.res.UserRegRes;
import com.jgxq.front.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-06
 */
public interface UserService extends IService<User> {

    User login(String email, String password);

    UserRegRes addUser(UserRegReq userReq);

    User getUserByPK(String col, String PK);
}
