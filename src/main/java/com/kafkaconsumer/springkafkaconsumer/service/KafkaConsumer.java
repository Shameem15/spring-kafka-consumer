package com.kafkaconsumer.springkafkaconsumer.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.repository.BooksRepository;

import lombok.extern.slf4j.Slf4j;

@Service
public class KafkaConsumer {

	/*
	 * @KafkaListener(topics = "Kafka_Example", groupId = "group_id") public void
	 * consume(String message) { System.out.println("Consumed message: " + message);
	 * }
	 */
	
	@Autowired
	private UrlHealthCheck urlHealthCheck;

	@Autowired
	private BooksRepository booksRepository;

	@Value("${elasticsearch.servers}")
	private String elasticServer;

	private static final String HTTP = "http://";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
	

	@KafkaListener(topics = "${kafka.books.topic}", groupId = "${kafka.group.id}", containerFactory = "userKafkaListenerFactory")
	public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition)
			throws JsonMappingException, JsonProcessingException {
		LOGGER.info("Received Message from kafka: \n" + message + "\nfrom partition: " + partition);

		urlHealthCheck.setUrl(HTTP + elasticServer);
		Health health = urlHealthCheck.getHealth(false);
		try {
			if (health.getStatus().getCode().equals("UP")) {

				ObjectMapper mapper = new ObjectMapper();
				List<Books> jsonListObject = mapper.readValue(message, new TypeReference<List<Books>>() {
				});

				booksRepository.saveAll(jsonListObject);
				LOGGER.info("save list of books object: " + jsonListObject);
			} else {
				LOGGER.error("Elastic Server is not UP. So, we can't save book Object");
			}
		} catch (Exception e) {
			LOGGER.error("While saving in the elastic server getting error: \n" + e);
		}
	}

}
