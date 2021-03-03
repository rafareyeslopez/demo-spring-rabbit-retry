/**
 *
 */
package com.demo;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Rafael Reyes Lopez
 * @email rafael.reyes.lopez@idbmobile.com
 * @date 2021-02-22
 *
 */
@Component
public class Producer {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	RabbitTemplate rabbitTemplate;

	int idNew = 1;

	int idUpdate = 1;

	Item item;

	@Scheduled(initialDelay = 1000, fixedRate = 160000)
	public void publishI() throws AmqpException, JsonProcessingException, InterruptedException {

		publishNewItem();

		Thread.sleep(1000);

		publishItemUpdate();

		Thread.sleep(1000);

		publishItemUpdate();

		Thread.sleep(1000);

		publishNewItem();

		Thread.sleep(1000);

	}

	private void publishItemUpdate() throws JsonProcessingException {
		item = new Item(Integer.toUnsignedLong(idUpdate), "update", "second value");

		System.out.println("Producing update of item " + item);

		rabbitTemplate.convertAndSend("notification-exchange", "notificationRoutingKey",
				objectMapper.writeValueAsString(item));

		idUpdate++;
	}

	private void publishNewItem() throws JsonProcessingException {
		item = new Item(Integer.toUnsignedLong(idNew), "new", "first value");

		System.out.println("Producing new item " + item);

		rabbitTemplate.convertAndSend("notification-exchange", "notificationRoutingKey",
				objectMapper.writeValueAsString(item));

		idNew++;
	}

}
