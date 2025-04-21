package com.snir.slide_show.service;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
public class SlideEventProducer {

    private final KafkaSender<String, String> kafkaSender;
    private final ObjectMapper mapper = new ObjectMapper();

    public SlideEventProducer(KafkaSender<String, String> kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    public Mono<Void> sendProofOfPlayEvent(int slideShowId, int imageId) {
        Map<String, Object> event = Map.of(
            "slideShowId", slideShowId,
            "imageId", imageId
        );
        String payload;
        try {
            payload = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException("Failed to serialize event", e));
        }

        SenderRecord<String, String, String> record =
            SenderRecord.create(new ProducerRecord<>("proof-of-play-events", null, payload), null);

        return kafkaSender.send(Mono.just(record)).then();
    }
}


