package io.github.twinklekhj.mqclient;

import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import io.github.twinklekhj.mqclient.activemq.service.ChatService;
import jakarta.jms.JMSException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MqclientApplicationTests {
    private final ChatService chatService;

    @Autowired
    MqclientApplicationTests(ChatService chatService) {
        this.chatService = chatService;
    }

    @Test
    @DisplayName("Queue 테스트")
    void topicQueue() throws JMSException {
        ChatMessage message = ChatMessage.builder()
                .userId("twinklekhj")
                .msg("안녕하세요!")
                .date(LocalDateTime.now())
                .build();

        Assertions.assertTrue(chatService.sendQueue(message), "Queue 보내기 실패");
    }
    @Test
    @DisplayName("Topic 테스트")
    void topicTopic() throws JMSException {
        ChatMessage message = ChatMessage.builder()
                .userId("twinklekhj")
                .msg("좋은 하루 보내세요~")
                .date(LocalDateTime.now())
                .build();

        Assertions.assertTrue(chatService.sendTopic(message), "Topic 보내기 실패");
    }

}
