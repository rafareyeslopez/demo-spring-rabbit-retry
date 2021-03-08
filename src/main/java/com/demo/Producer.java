/**
 *
 */
package com.demo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Rafael Reyes Lopez
 * @email rafael.reyes.lopez@idbmobile.com
 * @date 2021-02-22
 *
 */
@Component
@Slf4j
public class Producer {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	private Queue queue;

	int idNew = 1;

	int idUpdate = 1;

	Item item;

	public void publish() throws AmqpException, JsonProcessingException, InterruptedException {

		publishNewItem();

		Thread.sleep(1000);

		publishItemUpdate();

		Thread.sleep(5000);

		publishItemUpdate();

		Thread.sleep(1000);

		publishNewItem();

		Thread.sleep(1000);

	}

	private void publishItemUpdate() throws JsonProcessingException {
		item = new Item(Integer.toUnsignedLong(idUpdate), "update", "second value");

		log.info("Producing update of item " + item);

		rabbitTemplate.convertAndSend("notification", objectMapper.writeValueAsString(item));

		idUpdate++;
	}

	private void publishNewItem() throws JsonProcessingException {
		item = new Item(Integer.toUnsignedLong(idNew), "new", "first value");

		log.info("Producing new item " + item);

		rabbitTemplate.convertAndSend(queue.getName(), objectMapper.writeValueAsString(item));

		idNew++;
	}

}
