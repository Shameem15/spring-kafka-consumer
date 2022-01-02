package com.kafkaconsumer.springkafkaconsumer.service;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;

import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.repository.BooksRepository;

@SpringBootTest
class ElasticBookServiceTest {

	@InjectMocks
	private ElasticBookService elasticBookService;
	
	@Mock
	private BooksRepository booksRepository;
	
	@Mock
	private UrlHealthCheck urlHealthCheck;
	
	
	@Test
	public void test_getAllBooks() throws Exception {
		
		List<Books> bookslist = getAllBooks();
		Mockito.when(booksRepository.findAll()).thenReturn(bookslist);
		Mockito.when(urlHealthCheck.getHealth(false)).thenReturn(Health.up().build());
		
		List<Books> result = elasticBookService.getAllBooks();
		Assert.assertEquals(bookslist.toString(),  result.toString());
		
		Mockito.verify(booksRepository, times(1)).findAll();
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
