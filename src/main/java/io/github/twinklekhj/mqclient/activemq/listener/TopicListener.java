package io.github.twinklekhj.mqclient.activemq.listener;

import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TopicListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        log.info("[Topic Object] receive message: {}", message);
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            ChatMessage body = null;
            try {
                body = textMessage.getBody(ChatMessage.class);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            log.info("[Topic Object] receive body: {}", body);
        }
    }
}
