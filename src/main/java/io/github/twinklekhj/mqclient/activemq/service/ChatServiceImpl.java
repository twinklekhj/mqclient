package io.github.twinklekhj.mqclient.activemq.service;

import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import jakarta.jms.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    private final JmsTemplate template;
    private final Queue chatQueue;
    private final Topic chatTopic;

    @Override
    public boolean send(Destination destination, ChatMessage message) {
        try {
            template.convertAndSend(destination, message);

            return true;
        } catch (JmsException exception) {
            log.error("JMS Exception 발생: {}", exception.getMessage());
            exception.getStackTrace();
            return false;
        }
    }

    @Override
    public boolean sendQueue(ChatMessage message) throws JMSException {
        log.info("[Queue] name: {}, send message: {}", chatQueue.getQueueName(), message);
        return send(chatQueue, message);
    }

    @Override
    public boolean sendTopic(ChatMessage message) throws JMSException {
        log.info("[Topic] name: {}, send message: {}", chatTopic.getTopicName(), message);
        return send(chatTopic, message);
    }
}
