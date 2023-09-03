package com.xuecheng.learning.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.learning.config.PayNotifyConfig;
import com.xuecheng.learning.service.MyCourseTableService;
import com.xuecheng.messagesdk.model.po.MqMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

public class ReceivePayNotifyService {
    @Autowired
    MyCourseTableService myCourseTablesService;
    @RabbitListener(queues = PayNotifyConfig.PAYNOTIFY_QUEUE)
    public void receive(Message message) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] body = message.getBody();
        String jsonString = new String(body);
        MqMessage mqMessage = JSON.parseObject(jsonString, MqMessage.class);
        String messageType = mqMessage.getMessageType();
        String chooseCourseId = mqMessage.getBusinessKey1();
        String orderType = mqMessage.getBusinessKey2();
        if (orderType.equals("60201")) {
            //根据消息内容，更新选课记录、向我的课程表插入记录
            boolean b = myCourseTablesService.saveChooseCourseSuccess(chooseCourseId);
            if (!b) {
                XueChengPlusException.cast("保证选课记录状态失败");
            }
        }
    }
}
