package com.tryton.tut.tut_spring_kafka_streams_travis_credits.controller;

import com.tryton.tut.tut_spring_kafka_streams_travis_credits.service.CreditBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/travis-credit-balance")
public class CreditBalanceCalculatorController {
    private final CreditBalanceService creditBalanceService;

    @PostMapping(value = "/start-calculating")
    public void start() {
        creditBalanceService.start();
    }

	@PostMapping(value = "/stop-calculating")
    public void stop() {
        creditBalanceService.stop();
    }
}
