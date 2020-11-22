package com.abhinotes.m2o.connector.sink.config;

import com.abhinotes.m2o.commons.entity.JMSMessageForKafka;
import com.abhinotes.m2o.connector.sink.client.M2OConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
public class M2OConsumerConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(M2OConsumerConfig.class);


    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.application.name}")
    private String consumerApplicationname;
    @Value("${m2o.sink.environment}")
    private String environment;

    @Bean
    public ConsumerFactory<String, JMSMessageForKafka> consumerFactory() {

        JsonDeserializer<JMSMessageForKafka> jmsMsgForKafkaDeserializer = new JsonDeserializer<>(JMSMessageForKafka.class);
        jmsMsgForKafkaDeserializer.setRemoveTypeHeaders(false);
        jmsMsgForKafkaDeserializer.addTrustedPackages("com.abhinotes.m2o.commons.entity");
        jmsMsgForKafkaDeserializer.setUseTypeMapperForKey(true);

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "M2OClient-"+ environment+ UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jmsMsgForKafkaDeserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerApplicationname);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        LOGGER.debug(String.format("Consumer Properties : {%s}", props.toString()));

        return new DefaultKafkaConsumerFactory<>(props,new StringDeserializer(),jmsMsgForKafkaDeserializer);

    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, JMSMessageForKafka>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JMSMessageForKafka> consumerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        consumerFactory.setConsumerFactory(consumerFactory());
        return consumerFactory;
    }

    @Bean
    public M2OConsumer mrConsumer() {
        return new M2OConsumer();
    }

}
