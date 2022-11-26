package com.tryton.tut.tut_spring_kafka_streams_travis_credits.controller;

import com.launchdarkly.eventsource.EventSource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/wikimedia/producer/")
public class EventSourceController {

	private final EventSource eventSource;

	@PostMapping(value = "/start")
	public void start() {
		eventSource.start();
	}

	@PostMapping(value = "/stop")
	public void stop() {
		eventSource.close();
	}
}
