package com.abhinotes.kafka.streams.processor.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class StreamsConfigurations {

    @Value("${streams.kafka.threads:1}")
    private int threads;

    @Value("${streams.kafka.replication-factor:1}")
    private int replicationFactor;

    @Value("${streams.kafka.bootstrap-servers}")
    private String brokersUrl;

    @Value("${streams.kafka.app.id}")
    private String streamsApp;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> config = new HashMap<>();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "MessageRouterApplication");
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 30000);
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
        config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        setDefaults(config);
        return new KafkaStreamsConfiguration(config);
    }

    public void setDefaults(Map<String, Object> config) {
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, FailOnInvalidTimestamp.class);
    }

    @Bean
    public StreamsBuilderFactoryBeanCustomizer customizer() {
        return fb -> fb.setStateListener((newState, oldState) -> {
        });
    }


}

