package com.tryton.tut.tut_spring_kafka_streams_travis_credits.config;

import com.tryton.tut.tut_spring_kafka_streams_travis_credits.service.StatefulCreditUsageGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig {

	private final KafkaProperties kafkaProperties;
	@Bean
	public Properties kafkaProducerProperties() {
		// create Producer Properties
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		// set safe producer configs (Kafka <= 2.8)
		properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
		properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // same as setting -1
		properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE)); // same as setting -1

		// set high throughput producer configs
		properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
		properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32 * 1024));
		properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

		return properties;
	}

	@Bean
	public StatefulCreditUsageGenerator statefulCreditUsageGenerator() {
		return new StatefulCreditUsageGenerator(kafkaProperties, kafkaProducerProperties());
	}
}
