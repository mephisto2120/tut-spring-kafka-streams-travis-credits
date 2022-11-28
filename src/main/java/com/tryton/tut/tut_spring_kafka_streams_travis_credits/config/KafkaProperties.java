package com.tryton.tut.tut_spring_kafka_streams_travis_credits.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka")
public class KafkaProperties {
    private String inputKafkaTopic;
    private String outputKafkaTopic;
    private String bootstrapServers;
}
