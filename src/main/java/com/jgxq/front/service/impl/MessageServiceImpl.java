package com.jgxq.front.service.impl;

import com.jgxq.front.entity.Message;
import com.jgxq.front.mapper.MessageMapper;
import com.jgxq.front.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
