package com.tryton.tut.tut_spring_kafka_streams_travis_credits.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tryton.tut.tut_spring_kafka_streams_travis_credits.config.KafkaProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@CommonsLog
public class StatefulCreditUsageGenerator {

    private final KafkaProperties kafkaProperties;
    private final Properties config;
    private Producer<String, String> producer;
    private boolean enabled = false;

    public void start() {
        enabled = true;
        producer = new KafkaProducer<>(config);
        int i = 0;
        while (enabled) {
            log.info(String.format("Producing batch: %d", i));
            try {
                producer.send(newRandomCreditUsage("boris-123"));
                Thread.sleep(100);
                producer.send(newRandomCreditUsage("anatolij-456"));
                Thread.sleep(100);
                producer.send(newRandomCreditUsage("garri-789"));
                Thread.sleep(100);
                i += 1;
            } catch (InterruptedException e) {
                break;
            }
        }
        if (!enabled) {
            producer.close();
            log.info("Producer has been closed");
        }
    }

    private ProducerRecord<String, String> newRandomCreditUsage(String uuid) {
        // creates an empty json {}
        ObjectNode creditUsage = JsonNodeFactory.instance.objectNode();

        // { "amount" : 46 } (46 is a random number between 0 and 100 excluded)
        Integer amount = ThreadLocalRandom.current().nextInt(0, 100);

        // Instant.now() is to get the current time using Java 8
        Instant now = Instant.now();

        // we write the data to the json document
        creditUsage.put("uuid", uuid);
        creditUsage.put("amount", amount);
        creditUsage.put("time", now.toString());
        return new ProducerRecord<>(kafkaProperties.getInputKafkaTopic(), uuid, creditUsage.toString());
    }

    public void stop() {
        enabled = false;
    }
}
