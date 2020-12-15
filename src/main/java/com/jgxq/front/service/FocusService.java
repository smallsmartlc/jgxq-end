package com.jgxq.front.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jgxq.common.res.UserFocusRes;
import com.jgxq.front.entity.Focus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
public interface FocusService extends IService<Focus> {

    Page<UserFocusRes> pageToResPage(Page<Focus> page, String userKey);
}
