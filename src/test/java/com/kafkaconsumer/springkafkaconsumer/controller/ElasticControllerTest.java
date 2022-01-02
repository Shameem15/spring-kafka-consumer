package com.kafkaconsumer.springkafkaconsumer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.service.ElasticBookService;
import com.kafkaconsumer.springkafkaconsumer.service.KafkaConsumer;


@SpringBootTest
class ElasticControllerTest {

	
	@InjectMocks
	private ElasticController controller;
	
	@Mock
	private ElasticBookService elasticBookService;
	
	@Test
	public void test_getAllBooksDetails() throws Exception {
		
		List<Books> bookslist = getAllBooks();
		Mockito.when(elasticBookService.getAllBooks()).thenReturn(bookslist);
		
		ResponseEntity<List<Books>> result = controller.getAllBooksDetails();
		Assert.assertEquals(HttpStatus.OK,  result.getStatusCode());
		
		Mockito.verify(elasticBookService, times(1)).getAllBooks();
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
