package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.front.entity.Collect;
import com.jgxq.front.mapper.CollectMapper;
import com.jgxq.front.service.CollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public Boolean collect(Byte type, Integer id, String userkey, boolean collected) {
        int flag;
        if (collected) {
            UpdateWrapper<Collect> collectUpdate = new UpdateWrapper<>();
            collectUpdate.eq("type",type)
                    .eq("obj_id",id)
                    .eq("userkey",userkey);
            flag = collectMapper.delete(collectUpdate);
        } else {
            Collect c = new Collect();
            c.setType(type);
            c.setObjId(id);
            c.setUserkey(userkey);
            try {
                flag = collectMapper.insert(c);
            } catch (DuplicateKeyException e) {
                flag = -1;
            }
        }
        return flag > 0;
    }
}
