package com.xywei.rabbitmq;

import com.rabbitmq.client.*;

/**
 * Project:rabbitmq-demo
 * File:com.xywei.rabbitmq
 * Author:xywei
 * Email :weixiangyu@homolo.com
 * Copyright 2004-2018 Homolo Co., Ltd. All rights reserved.
 */
public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        String message = "8....";

        // deliveryMode   1:非持久化  2 :持久化
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.deliveryMode(2);
        AMQP.BasicProperties properties = builder.build();

        //MessageProperties.PERSISTENT_TEXT_PLAIN  与 MessageProperties.PERSISTENT_TEXT_PLAIN 其实是等价的

        channel.basicPublish("", TASK_QUEUE_NAME,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
