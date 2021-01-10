package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.UserFocusRes;
import com.jgxq.front.entity.Focus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
public interface FocusService extends IService<Focus> {

    List<UserFocusRes> keyListToRes(List<String> keyList, String userKey);
}
