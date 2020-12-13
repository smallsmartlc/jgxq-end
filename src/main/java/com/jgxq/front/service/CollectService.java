package com.jgxq.front.service;

import com.jgxq.front.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
public interface CollectService extends IService<Collect> {

    Boolean collect(Byte type, Integer id, String userkey, boolean collected);
}
