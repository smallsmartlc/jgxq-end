package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jgxq.common.req.UserFindPasswordReq;
import com.jgxq.common.req.UserRegReq;
import com.jgxq.common.res.*;
import com.jgxq.common.utils.LoginUtils;
import com.jgxq.common.utils.PasswordHash;
import com.jgxq.core.entity.AuthContext;
import com.jgxq.front.define.BooleanEnum;
import com.jgxq.front.define.KeyLength;
import com.jgxq.front.entity.*;
import com.jgxq.front.mapper.CommentMapper;
import com.jgxq.front.mapper.FocusMapper;
import com.jgxq.front.mapper.TalkMapper;
import com.jgxq.front.mapper.UserMapper;
import com.jgxq.front.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private FocusMapper focusMapper;

    @Override
    public User login(String email, String password) {

        User userReq = new User();
        userReq.setEmail(email);

        User user = userMapper.selectOne(new QueryWrapper<>(userReq));
        if (user == null) {
            return null;
        }
        boolean equal;
        try {
            equal = PasswordHash.validatePassword(password, user.getPassword());
        } catch (Exception e) {
            equal = false;
        }
        if (equal) {
            return user;
        }
        return null;
    }

    @Override
    public String addUser(UserRegReq userReq) {

        try {
            userReq.setPassword(PasswordHash.createHash(userReq.getPassword()));
        } catch (Exception e) {
            return null;
        }

        User user = new User();
        user.setUserkey(LoginUtils.getRandomUserKey(KeyLength.USER_KEY_LEN.getLength()));
        BeanUtils.copyProperties(userReq, user);

        userMapper.insert(user);
        return user.getUserkey();
    }

    @Override
    public boolean updatePassword(UserFindPasswordReq userReq) {
        try {
            userReq.setPassword(PasswordHash.createHash(userReq.getPassword()));
        } catch (Exception e) {
            return false;
        }
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("email", userReq.getEmail())
                .set("password", userReq.getPassword());
        int flag = userMapper.update(null, wrapper);
        return flag > 0;
    }

    @Override
    public UserActiveRes getUserActiveRes(String target) {
        UserLoginRes user = userToLoginRes(getUserByPK("userkey",target));
        if(user == null){
            return null;
        }
        UserActiveRes res = new UserActiveRes();
        res.setUserInfo(user);
        QueryWrapper<Talk> talkQuery = new QueryWrapper<>();
        QueryWrapper<Comment> commentQuery = new QueryWrapper<>();
        QueryWrapper<Focus> focusQuery = new QueryWrapper<>();
        QueryWrapper<Focus> fansQuery = new QueryWrapper<>();

        talkQuery.eq("author",target);
        commentQuery.eq("userkey",target);
        focusQuery.eq("userkey",target);
        fansQuery.eq("target",target);

        res.setTalks(talkMapper.selectCount(talkQuery));
        res.setComments(commentMapper.selectCount(commentQuery));
        res.setFocus(focusMapper.selectCount(focusQuery));
        res.setFans(focusMapper.selectCount(fansQuery));

        return res;
    }

    @Override
    public User getUserByPK(String col, String PK) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(col, PK);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    public UserLoginRes userToLoginRes(User user) {
        TeamBasicRes team = teamService.getBasicTeamById(user.getHomeTeam());
        UserLoginRes userRes = new UserLoginRes();
        BeanUtils.copyProperties(user,userRes);
        userRes.setAuthor(user.getAuthor().equals(BooleanEnum.True.getValue()));
        userRes.setHomeTeam(team);
        return userRes;
    }

    public List<UserLoginRes> getUserInfoByKeyList(Collection<String> userkeyList) {
        if(userkeyList.isEmpty()){
            return Collections.emptyList();
        }
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.in("userkey", userkeyList);
        List<User> users = userMapper.selectList(userQuery);
        Set<Integer> homeTeams = users.stream().map(User::getHomeTeam).collect(Collectors.toSet());
        List<TeamBasicRes> basicTeamByIds = teamService.getBasicTeamByIds(homeTeams);
        Map<Integer, TeamBasicRes> teamMap = basicTeamByIds.stream().collect(Collectors.toMap(TeamBasicRes::getId, t -> t));

        List<UserLoginRes> resList = users.stream().map(u -> {
            UserLoginRes res = new UserLoginRes();
            BeanUtils.copyProperties(u, res);
            res.setHomeTeam(teamMap.get(u.getId()));
            return res;
        }).collect(Collectors.toList());
        return resList;
    }

    public List<UserBasicRes> getUserBasicByKeyList(Collection<String> userkeyList){
        if(userkeyList.isEmpty()){
            return Collections.emptyList();
        }
        QueryWrapper<User> userQuery = new QueryWrapper<>();
        userQuery.select("userkey","nick_name","head_image")
                .in("userkey",userkeyList);
        List<User> userList = userMapper.selectList(userQuery);
        List<UserBasicRes> resList = userList.stream().map(u -> {
            UserBasicRes res = new UserBasicRes();
            BeanUtils.copyProperties(u, res);
            return res;
        }).collect(Collectors.toList());

        return resList;
    }

    public AuthorRes getAuthorInfo(String userKey) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select("userkey", "nick_name")
                .eq("userkey", userKey);
        User user = userMapper.selectOne(wrapper);
        AuthorRes res = new AuthorRes();
        res.setNickName(user.getNickName());
        res.setUserkey(userKey);
        return res;
    }

    public AuthContext getAuthContextByKey(HttpServletRequest request, String userkey) {
        User user = new User();
        user.setUserkey(userkey);
        User res = userMapper.selectOne(new QueryWrapper<>(user));
        AuthContext authContext = new AuthContext();
        BeanUtils.copyProperties(res, authContext);
        return authContext;
    }
}
