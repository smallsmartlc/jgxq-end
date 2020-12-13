package com.jgxq.front.service;

import com.jgxq.front.entity.Thumb;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
public interface ThumbService extends IService<Thumb> {

    Boolean thumb(Byte type, Integer id, String userkey);
}
