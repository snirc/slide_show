package com.snir.slide_show.service;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public KafkaSender<String, String> kafkaSender() {
		Map<String, Object> props = Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		SenderOptions<String, String> senderOptions = SenderOptions.create(props);
		return KafkaSender.create(senderOptions);
	}
}
