package com.snir.slide_show.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;


/**
 * KafkaEventService is a service class that handles sending events to a Kafka topic.
 * It uses the KafkaSender to send messages asynchronously.
 */
@Service
public class KafkaEventService {

    private final KafkaSender<String, String> kafkaSender;

    public KafkaEventService(KafkaSender<String, String> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    /**
	 * Sends an event to the specified Kafka topic with the given key and value.
	 *
	 * @param topic The Kafka topic to send the event to.
	 * @param key   The key for the Kafka message.
	 * @param value The value for the Kafka message.
	 * @return A Mono that completes when the event is sent.
	 */
    public Mono<Void> sendEvent(String topic, String key, String value) {
        SenderRecord<String, String, String> record =
            SenderRecord.create(new ProducerRecord<>(topic, key, value), key);

        return kafkaSender.send(Mono.just(record))
                .doOnNext(result -> System.out.println("Sent: " + result.recordMetadata()))
                .then();
    }
}

