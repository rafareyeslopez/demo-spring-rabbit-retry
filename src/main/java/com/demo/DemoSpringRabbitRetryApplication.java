package com.demo;

import javax.annotation.PostConstruct;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DemoSpringRabbitRetryApplication {

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	Producer producer;

	public static void main(final String[] args) {
		SpringApplication.run(DemoSpringRabbitRetryApplication.class, args);
	}

	@Bean
	Queue queue() {
		return new Queue("notification", false);
	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}

	@PostConstruct
	public void init() throws AmqpException, JsonProcessingException, InterruptedException {

		amqpAdmin.declareQueue(queue());

		producer.publish();
	}

}
