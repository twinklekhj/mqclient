package io.github.twinklekhj.mqclient.activemq.config;

import io.github.twinklekhj.mqclient.activemq.listener.TopicListener;
import io.github.twinklekhj.mqclient.activemq.message.ChatMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ActiveMQProperties.class)
@Slf4j
public class ActiveMQConfig {
    @Bean
    public ActiveMQConnectionFactory connectionFactory(ActiveMQProperties properties) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(properties.getBrokerUrl());
        factory.setUserName(properties.getUser());
        factory.setPassword(properties.getPassword());
        factory.setCloseTimeout(properties.getCloseTimeout().toMillisPart());

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory factory, MappingJackson2MessageConverter messageConverter) {
        JmsTemplate template = new JmsTemplate(factory);
        template.setMessageConverter(messageConverter); // 메시지 Converter 설정

        template.setExplicitQosEnabled(true);    // 메시지 전송 시 QOS을 설정
        template.setDeliveryPersistent(false);   // 메시지의 영속성을 설정
        template.setReceiveTimeout(1000 * 3);    // 메시지를 수신하는 동안의 대기 시간을 설정(3초)
        template.setTimeToLive(1000 * 60 * 30);  // 메시지의 유효 기간을 설정(30분)
        return template;
    }


    @Bean
    public ActiveMQQueue chatQueue(@Value("${activemq.queue.chat}") String queueName) {
        return new ActiveMQQueue(queueName);
    }

    @Bean
    public Topic chatTopic(@Value("${activemq.topic.chat}") String topicName) {
        return new ActiveMQTopic(topicName);
    }

    @Bean
    public JmsListenerContainerFactory<?> queueContainerFactory(
            ActiveMQConnectionFactory connectionFactory, MappingJackson2MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicContainerFactory(
            ActiveMQConnectionFactory connectionFactory,
            MappingJackson2MessageConverter messageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setPubSubDomain(true);
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public DefaultMessageListenerContainer topicContainerFactory2(
            ActiveMQConnectionFactory factory, Topic topic, TopicListener listener) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.setDestination(topic);
        container.setMessageListener(listener);

        return container;
    }

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter =
                new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("chat", ChatMessage.class);

        messageConverter.setTypeIdMappings(typeIdMappings);

        return messageConverter;
    }

}
