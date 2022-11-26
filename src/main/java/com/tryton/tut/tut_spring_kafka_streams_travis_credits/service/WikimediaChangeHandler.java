package com.tryton.tut.tut_spring_kafka_streams_travis_credits.service;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

@CommonsLog
@RequiredArgsConstructor
public class WikimediaChangeHandler implements EventHandler {

	private final String topic;
    private final Producer<String, String> kafkaProducer;


    @Override
    public void onOpen() {
        // nothing here
    }

    @Override
    public void onClosed() {
        kafkaProducer.close();
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        log.info(messageEvent.getData());
        // asynchronous
        kafkaProducer.send(new ProducerRecord<>(topic, messageEvent.getData()));
    }

    @Override
    public void onComment(String comment) {
        // nothing here
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in Stream Reading", t);
    }
}
