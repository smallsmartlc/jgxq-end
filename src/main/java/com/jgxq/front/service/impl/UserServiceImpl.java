package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.common.req.UserRegReq;
import com.jgxq.common.res.UserRegRes;
import com.jgxq.common.utils.LoginUtils;
import com.jgxq.common.utils.PasswordHash;
import com.jgxq.front.define.KeyLengthEnum;
import com.jgxq.front.entity.User;
import com.jgxq.front.mapper.UserMapper;
import com.jgxq.front.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public UserRegRes addUser(UserRegReq userReq) {

        try {
            userReq.setPassword(PasswordHash.createHash(userReq.getPassword()));
        } catch (Exception e) {
            return null;
        }

        User user = new User();
        user.setUserkey(LoginUtils.getRandomUserKey(KeyLengthEnum.USER_KEY_LEN.getLength()));
        BeanUtils.copyProperties(userReq,user);

        userMapper.insert(user);
        UserRegRes userRes = new UserRegRes();
        userRes.setId(user.getId());
        userRes.setUserkey(user.getUserkey());

        return userRes;
    }
}
