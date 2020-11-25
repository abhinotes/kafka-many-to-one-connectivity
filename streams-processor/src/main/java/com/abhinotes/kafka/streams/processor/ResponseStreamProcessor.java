package com.abhinotes.kafka.streams.processor;

import com.abhinotes.m2o.commons.constants.ConfigurationConstants;
import com.abhinotes.m2o.commons.entity.M2OMessageFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

@Component
public class ResponseStreamProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseStreamProcessor.class);

    private static final String STATE_DIR = "state.dir";
    private static final String APPID = "ManyToOneStreamsProcessor-";
    private static final String EARLIEST = "latest";

    private final String stateDir;
    private final String accountingRequestTopic;
    private final String accountingResponseTopic;
    private final String bootstrapServers;

    @Autowired
    public ResponseStreamProcessor(@Value("${state.dir}") String stateDir,
                                   @Value("${m2o.kafka.topic.request}") String accountingRequestTopic,
                                   @Value("${m2o.kafka.topic.response}") String accountingResponseTopic,
                                   @Value("${streams.kafka.bootstrap-servers}") String bootstrapServers ) {
        this.stateDir = stateDir;
        this.accountingRequestTopic = accountingRequestTopic;
        this.accountingResponseTopic = accountingResponseTopic;
        this.bootstrapServers = bootstrapServers;

    }


    @Bean(name = "M2OKafkaStreamProcessorBean")
    private void startStreamsProcessor() {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, UUID.randomUUID() + APPID);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, EARLIEST);
        config.put(StreamsConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        config.put(STATE_DIR, stateDir);
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        // Create request as GlobalKTable - As request can be on any partition.
        // Response topic as KStream - As triggers are response messages
        GlobalKTable<String, String> requestStream = builder.globalTable(accountingRequestTopic);
        KStream<String, String> responseStream = builder.stream(accountingResponseTopic);

        // Define output stream by joining response stream with
        KStream<String, String> processStream = responseStream.join(requestStream, (key, value) -> key,
                (res, req) -> getUpdatedResponse(req, res));

        // Destination Route derivation and Posting :  Write topic derivation logic (Ex:- topicNameExtractor)
        processStream.to((key, value, recordContext) -> topicNameExtractor(value));

        @SuppressWarnings({ "resource" })
        KafkaStreams streamToProcess = new KafkaStreams(builder.build(), config);
        streamToProcess.cleanUp();
        streamToProcess.start();

        // Shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(streamToProcess::close));
    }

    private  String topicNameExtractor(String response) {
        LOGGER.info(String.format("Topic Name Extractor for payload -> %s", response));
        ObjectMapper objectMapper = new ObjectMapper();
        M2OMessageFormat kafkaResponseMessage = null;
        try {
            kafkaResponseMessage = objectMapper.readValue(response, M2OMessageFormat.class);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error while processing incoming message >> %s", e.getMessage()));
        }
        if (kafkaResponseMessage != null) {
            return accountingResponseTopic.concat(kafkaResponseMessage.getSource());
        } else {
            return accountingResponseTopic.concat("ERROR");
        }
    }

    private static String getUpdatedResponse(String request, String response) {
        LOGGER.info(String.format("Updating Response using [Req],[Resp] :: [%s],[%s]", request, response));

        ObjectMapper objectMapper = new ObjectMapper();
        M2OMessageFormat kafkaRequestMessage;
        M2OMessageFormat kafkaResponseMessage;
        String updatedResponse = null;
        try {
            kafkaRequestMessage = objectMapper.readValue(request, M2OMessageFormat.class);
            kafkaResponseMessage = objectMapper.readValue(response, M2OMessageFormat.class);
            kafkaResponseMessage.setSource(kafkaRequestMessage.getSource());
            updatedResponse = objectMapper.writeValueAsString(kafkaResponseMessage);
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Error while processing incoming message >> %s", e.getMessage()));
        }
        return updatedResponse;
    }

}
