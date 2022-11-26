package com.tryton.tut.tut_spring_kafka_streams_travis_credits.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Properties;

@RequiredArgsConstructor
public class CreditBalanceService {
	private static final String TRAVIS_CREDITS_TOPIC = "travis-credits";
	private final Properties config;

	private KafkaStreams streams;

	public void start() {
		// json Serde
		final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
		final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
		final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);

		StreamsBuilder builder = new StreamsBuilder();

		KStream<String, JsonNode> bankTransactions = builder.stream(TRAVIS_CREDITS_TOPIC,
				Consumed.with(Serdes.String(), jsonSerde));


		// create the initial json object for balances
		ObjectNode initialBalance = JsonNodeFactory.instance.objectNode();
		initialBalance.put("count", 0);
		initialBalance.put("balance", 0);
		initialBalance.put("time", Instant.ofEpochMilli(0L).toString());

		KTable<String, JsonNode> bankBalance = bankTransactions
				.groupByKey(Serialized.with(Serdes.String(), jsonSerde))
				.aggregate(
						() -> initialBalance,
						(key, transaction, balance) -> newBalance(transaction, balance),
						Materialized.<String, JsonNode, KeyValueStore<Bytes, byte[]>>as("travis-credits-agg")
								.withKeySerde(Serdes.String())
								.withValueSerde(jsonSerde)
				);

		bankBalance.toStream().to("travis-credits-exactly-once", Produced.with(Serdes.String(), jsonSerde));

		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.cleanUp();
		streams.start();

		// print the topology
		streams.localThreadsMetadata().forEach(data -> System.out.println(data));
	}

	private static JsonNode newBalance(JsonNode transaction, JsonNode balance) {
		// create a new balance json object
		ObjectNode newBalance = JsonNodeFactory.instance.objectNode();
		newBalance.put("count", balance.get("count").asInt() + 1);
		newBalance.put("balance", balance.get("balance").asInt() + transaction.get("amount").asInt());

		Long balanceEpoch = Instant.parse(balance.get("time").asText()).toEpochMilli();
		Long transactionEpoch = Instant.parse(transaction.get("time").asText()).toEpochMilli();
		Instant newBalanceInstant = Instant.ofEpochMilli(Math.max(balanceEpoch, transactionEpoch));
		newBalance.put("time", newBalanceInstant.toString());
		return newBalance;
	}

	public void stop() {
		streams.close();
	}
}
