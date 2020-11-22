package com.abhinotes.kafka.streams.processor;

import com.abhinotes.m2o.commons.constants.ConfigurationConstants;
import com.abhinotes.m2o.commons.entity.JMSMessageForKafka;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

@Component
public class ResponseStreamProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseStreamProcessor.class);

    private static final String STATE_DIR = "state.dir";
    private static final String APPID = "ManyToOneStreamsProcessor-";
    private static final String EARLIEST = "earliest";
    private static final String REQUEST = "requestTopicGlobal";
    private static final String RESPONSE = "responseTopicGlobal";
    private static final String ENVIRONMENTBASE = "responseTopic";

    @Bean(name = "MessageRouterKafkaStreamProcessorBean")
    private void startStreamsProcessor() {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID() + APPID);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ConfigurationConstants.BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, EARLIEST);
        config.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        config.put(STATE_DIR, "/Users/magnet/Apps/kafka_2.13-2.6.0/kafka-state");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        GlobalKTable<String, String> requestStream = builder.globalTable(REQUEST);
        KStream<String, String> responseStream = builder.stream(RESPONSE);

        KStream<String, String> processStream = responseStream.join(requestStream, (key, value) -> key,
                (res, req) -> getUpdatedResponse(req, res));

        processStream.to((key, value, recordContext) -> topicNameExtractor(value));

        @SuppressWarnings({ "resource" })
        KafkaStreams streamToProcess = new KafkaStreams(builder.build(), config);
        streamToProcess.cleanUp();
        streamToProcess.start();

        // Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(streamToProcess::close));
    }

    private static String topicNameExtractor(String response) {
        LOGGER.info(String.format("Topic Name Extractor for payload -> %s", response));
        ObjectMapper objectMapper = new ObjectMapper();
        JMSMessageForKafka kafkaResponseMessage = null;
        try {
            kafkaResponseMessage = objectMapper.readValue(response, JMSMessageForKafka.class);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error while processing incoming message >> %s", e.getMessage()));
        }
        if (kafkaResponseMessage != null) {
            return ENVIRONMENTBASE.concat(kafkaResponseMessage.getSource());
        } else {
            return ENVIRONMENTBASE.concat("ERROR");
        }
    }

    private static String getUpdatedResponse(String request, String response) {
        LOGGER.info(String.format("Updating Response using [Req],[Resp] :: [%s],[%s]", request, response));

        ObjectMapper objectMapper = new ObjectMapper();
        JMSMessageForKafka kafkaRequestMessage;
        JMSMessageForKafka kafkaResponseMessage;
        String updatedResponse = null;
        try {
            kafkaRequestMessage = objectMapper.readValue(request, JMSMessageForKafka.class);
            kafkaResponseMessage = objectMapper.readValue(response, JMSMessageForKafka.class);
            kafkaResponseMessage.setSource(kafkaRequestMessage.getSource());
            updatedResponse = objectMapper.writeValueAsString(kafkaResponseMessage);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error while processing incoming message >> %s", e.getMessage()));
        }
        return updatedResponse;
    }

}