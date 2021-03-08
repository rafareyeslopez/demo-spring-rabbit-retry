/*
 * Copyright (C) IDB Mobile Technology S.L. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package com.demo;

import javax.annotation.PostConstruct;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DemoSpringRabbitRetryApplication {

	public static final String NOTIFICATION_QUEUE = "notification";

	private static final String EXCHANGE_NAME = "notification-exchange";

	private static final String PARKINGLOT_QUEUE = NOTIFICATION_QUEUE + ".parkingLot";

	private static final String NOTIFICATION_ROUTING_KEY = "notificationRoutingKey";

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	Producer producer;

	public static void main(final String[] args) {
		SpringApplication.run(DemoSpringRabbitRetryApplication.class, args);
	}

	@Bean
	Queue notificationQueue() {
		return QueueBuilder.nonDurable(NOTIFICATION_QUEUE).deadLetterExchange(EXCHANGE_NAME)
				.deadLetterRoutingKey(PARKINGLOT_QUEUE).build();
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}

	@Bean
	Queue parkinglotQueue() {
		return new Queue(PARKINGLOT_QUEUE);
	}

	@Bean
	Binding primaryBinding(final Queue notificationQueue, final DirectExchange exchange) {
		return BindingBuilder.bind(notificationQueue).to(exchange).with(NOTIFICATION_ROUTING_KEY);
	}

	@Bean
	Binding parkingBinding(final Queue parkinglotQueue, final DirectExchange exchange) {
		return BindingBuilder.bind(parkinglotQueue).to(exchange).with(PARKINGLOT_QUEUE);
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

	@PostConstruct
	public void init() throws AmqpException, JsonProcessingException, InterruptedException {
		amqpAdmin.declareQueue(notificationQueue());

		producer.publish();
	}

}
