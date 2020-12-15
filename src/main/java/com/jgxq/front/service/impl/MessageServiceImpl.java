package com.jgxq.front.service.impl;

import com.jgxq.front.define.ObjectType;
import com.jgxq.front.define.MessageType;
import com.jgxq.front.entity.Message;
import com.jgxq.front.mapper.MessageMapper;
import com.jgxq.front.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author smallsmart
 * @since 2020-12-15
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private void sendMessage(String userKey, String target, MessageType messageType, Byte objectType, Integer objectId, String text) {
        Message message = new Message();
        message.setUserkey(userKey);
        message.setTarget(target);
        message.setMessageType(messageType.getValue());
        message.setObjectType(objectType);
        message.setObjectId(objectId);
        message.setText(messageType.getMessage() + text);
        save(message);
    }

    public void sendThumbMessage(String userKey, String target, Byte objectType, Integer objectId) {
        sendMessage(userKey, target, MessageType.thumb, objectType,objectId, "");
    }

    public void sendThumbMessage(String userKey, String target, Byte objectType, Integer objectId, String text) {
        sendMessage(userKey, target, MessageType.thumb, objectType,objectId, "的回复：" + text);
    }

    public void sendCommentMessage(String userKey, String target, Byte objectType, Integer objectId, String message) {
        sendMessage(userKey, target, MessageType.comment,objectType, objectId, message);
    }

}
