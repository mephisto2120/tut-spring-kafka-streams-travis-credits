package com.tryton.tut.tut_spring_kafka_streams_travis_credits.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@CommonsLog
public class StatefulCreditUsageGenerator {

	private static final String TRAVIS_CREDITS_TOPIC = "travis-credits";

	private final KafkaTemplate kafkaTemplate;
	private boolean enabled = false;

	public void start() {
		enabled = true;

		int i = 0;

		while (enabled) {
			log.info(String.format("Producing batch: %d", i));
			try {

				Map.Entry<String, String> entryUsage1 = newRandomCreditUsage("111");
				kafkaTemplate.send(TRAVIS_CREDITS_TOPIC, entryUsage1.getKey(), entryUsage1.getValue());
				Thread.sleep(100);
				Map.Entry<String, String> entryUsage2 = newRandomCreditUsage("222");
				kafkaTemplate.send(TRAVIS_CREDITS_TOPIC, entryUsage2.getKey(), entryUsage2.getValue());
				Thread.sleep(100);
				Map.Entry<String, String> entryUsage3 = newRandomCreditUsage("333");
				kafkaTemplate.send(TRAVIS_CREDITS_TOPIC, entryUsage3.getKey(), entryUsage3.getValue());
				Thread.sleep(100);
				i += 1;
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public static Map.Entry<String, String> newRandomCreditUsage(String uuid) {
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
		return new AbstractMap.SimpleImmutableEntry<>(uuid, creditUsage.toString());
	}

	public void stop() {
		enabled = false;
	}
}
