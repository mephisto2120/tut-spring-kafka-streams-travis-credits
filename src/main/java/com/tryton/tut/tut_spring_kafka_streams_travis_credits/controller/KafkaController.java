package com.tryton.tut.tut_spring_kafka_streams_travis_credits.controller;

import com.tryton.tut.tut_spring_kafka_streams_travis_credits.service.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final Producer producer;

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        producer.sendMessage(message);
    }
}
