/*
 * Copyright (C) IDB Mobile Technology S.L. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.demo;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Rafael Reyes Lopez
 * @email rafareyeslopez@gmail.com
 * @date 2021-02-22
 *
 */
@RabbitListener(queues = "notification")
@Component
@Slf4j
public class ItemConsumer {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ItemRepository itemRepository;

	@RabbitHandler
	public void onMessage(@Payload final String json) throws JsonMappingException, JsonProcessingException {

		log.info("Consuming " + json);
		final Item item = objectMapper.readValue(json, Item.class);

		if (item.getType().equals("new")) {
			log.info("Is a new Item " + json);
			itemRepository.save(item);

		} else {

			final Optional<Item> itemToBeUpdated = itemRepository.findById(item.getId());

			if (itemToBeUpdated.isPresent() && item.getType().equals("update")) {

				log.error("Item found for updating :) " + json);
				final Item itemToUpdate = itemToBeUpdated.get();

				itemToUpdate.setType(item.getType());
				itemToUpdate.setValue(item.getValue());
				itemRepository.save(itemToUpdate);

			} else {
				log.error("Item not found! :( " + json);
				throw new NoSuchElementException("Element not found to be updated " + json);
			}

		}

	}

}
