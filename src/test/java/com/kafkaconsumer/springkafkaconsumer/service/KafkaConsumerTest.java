package com.kafkaconsumer.springkafkaconsumer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.repository.BooksRepository;

@SpringBootTest
class KafkaConsumerTest {

	@InjectMocks
	private KafkaConsumer kafkaConsumer;
	
	@Mock
	private BooksRepository booksRepository;
	
	@Mock
	private UrlHealthCheck urlHealthCheck;
	
	@Test
	public void test_listenWithHeaders() throws JsonMappingException, JsonProcessingException {	
		String jsonString ="[{\"bookid\":1,\"bookname\":\"Programming with Java\",\"author\":\"E. Balagurusamy\",\"price\":350},{\"bookid\":2,\"bookname\":\"Programming with Java\",\"author\":\"E. Balagurusamy\",\"price\":450},{\"bookid\":3,\"bookname\":\"Programming with Java\",\"author\":\"E. Balagurusamy\",\"price\":550},{\"bookid\":4,\"bookname\":\"Data Structures and Algorithms in Java\",\"author\":\"Robert Lafore\",\"price\":590},{\"bookid\":5,\"bookname\":\"Effective Java\",\"author\":\"Joshua Bloch\",\"price\":670}]";
		List<Books> bookslist = getAllBooks();
		Mockito.when(booksRepository.saveAll(Mockito.anyList())).thenReturn(bookslist);
		Mockito.when(urlHealthCheck.getHealth(false)).thenReturn(Health.up().build());
		kafkaConsumer.listenWithHeaders(jsonString, 0);
		Mockito.verify(booksRepository, times(1)).saveAll(Mockito.anyList());
		Mockito.verify(urlHealthCheck, times(1)).getHealth(false);
	}
	
	private List<Books> getAllBooks() {
		Books book1 = new Books();
		book1.setBookid(1);
		book1.setBookname("Programming with Java");
		book1.setAuthor("E. Balagurusamy");
		book1.setPrice(320);
		
		Books book2 = new Books();
		book2.setBookid(2);
		book2.setBookname("Data Structures and Algorithms in Java");
		book2.setAuthor("Robert Lafore");
		book2.setPrice(590);
		
		Books book3 = new Books();
		book3.setBookid(3);
		book3.setBookname("Effective Java");
		book3.setAuthor("Joshua Bloch");
		book3.setPrice(670);
		
		
		List<Books> bookslist = new ArrayList<Books>();
		bookslist.add(book1);
		bookslist.add(book2);
		bookslist.add(book3);
		
		return bookslist;
	}

}
