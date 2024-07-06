package io.github.twinklekhj.mqclient.activemq.service;

import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;

public interface ChatService {
    boolean send(Destination destination, ChatMessage message);

    boolean sendQueue(ChatMessage message) throws JMSException;

    boolean sendTopic(ChatMessage message) throws JMSException;
}
