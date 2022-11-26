package com.tryton.tut.tut_spring_kafka_streams_travis_credits.config;

import com.tryton.tut.tut_spring_kafka_streams_travis_credits.service.CreditBalanceService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class StreamConfig {

    @Bean
    public Properties streamConfigProperties() {
        Properties config = new Properties();

        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "travis-credit-balance-application");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        // Exactly once processing!!
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

        return config;
    }

    @Bean
    public CreditBalanceService creditBalanceService() {
        return new CreditBalanceService(streamConfigProperties());
    }
}
