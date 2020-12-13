package com.jgxq.front.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.define.DeleteEnum;
import com.jgxq.front.define.ForumErrorCode;
import com.jgxq.front.entity.Thumb;
import com.jgxq.front.mapper.ThumbMapper;
import com.jgxq.front.service.ThumbService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-13
 */
@Service
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    @Autowired
    private ThumbMapper thumbMapper;

    @Override
    public Boolean thumb(Byte type, Integer id, String userkey) {
        Thumb thumb = new Thumb();
        thumb.setType(type);
        thumb.setUserkey(userkey);
        thumb.setObjectId(id);
        int flag;
        try {
            flag = thumbMapper.insert(thumb);
        }catch (DuplicateKeyException e){
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"您已经赞过了");
        }
        return flag > 0;
    }

}
