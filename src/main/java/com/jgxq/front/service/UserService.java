package com.jgxq.front.service;

import com.jgxq.common.res.UserRes;
import com.jgxq.front.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
}
