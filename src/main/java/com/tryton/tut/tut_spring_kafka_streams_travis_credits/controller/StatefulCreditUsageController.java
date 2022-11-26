package com.tryton.tut.tut_spring_kafka_streams_travis_credits.controller;

import com.tryton.tut.tut_spring_kafka_streams_travis_credits.service.StatefulCreditUsageGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/travis-credit-usage-generator")
public class KafkaController {
    private final StatefulCreditUsageGenerator statefulCreditUsageGenerator;

    @PostMapping(value = "/start")
    public void start() {
        statefulCreditUsageGenerator.start();
    }

	@PostMapping(value = "/stop")
    public void stop() {
        statefulCreditUsageGenerator.stop();
    }
}
