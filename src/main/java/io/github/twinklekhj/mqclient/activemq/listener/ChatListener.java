package io.github.twinklekhj.mqclient.activemq.listener;

import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatListener {
    @JmsListener(destination = "${activemq.queue.chat}", containerFactory = "queueContainerFactory")
    public void receiveQueue(ChatMessage message) {
        log.info("[Queue] receive message: {}", message);
    }

    @JmsListener(destination = "${activemq.topic.chat}", containerFactory = "topicContainerFactory")
    public void receiveTopic(ChatMessage message) {
        log.info("[Topic] receive message: {}", message);
    }
}
