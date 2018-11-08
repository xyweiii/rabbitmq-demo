package com.xywei.rabbitmq;

/**
 * Project:rabbitmq-demo
 * File:com.xywei.rabbitmq
 * Author:xywei
 * Email :weixiangyu@homolo.com
 * Copyright 2004-2018 Homolo Co., Ltd. All rights reserved.
 */

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws Exception {
        //建立连接和通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明路由器和路由器类型
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        //
        String bingingKeys[] = {"kern.*", "*.critical"};

        for (String bindingKey : bingingKeys) {
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
        }

        System.out.println(" [*] Waiting for kern.*, *.critical messages. To exit press CTRL+C");

        //监听消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
