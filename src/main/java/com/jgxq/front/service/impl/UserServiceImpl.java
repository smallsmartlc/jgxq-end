package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.res.UserRes;
import com.jgxq.common.utils.PasswordHash;
import com.jgxq.core.exception.SmartException;
import com.jgxq.front.entity.User;
import com.jgxq.front.mapper.UserMapper;
import com.jgxq.front.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String email, String password) {

        User userReq = new User();
        userReq.setEmail(email);

        User user = userMapper.selectOne(new QueryWrapper<>(userReq));
        if(user == null){
            return null;
        }
        boolean equal;
        try {
            equal = PasswordHash.validatePassword(password, user.getPassword());
        } catch (Exception e) {
            equal = false;
        }
        if(equal){
            return user;
        }
        return null;
    }
}
