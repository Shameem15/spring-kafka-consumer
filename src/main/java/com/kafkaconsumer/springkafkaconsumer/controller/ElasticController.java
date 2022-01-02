package com.kafkaconsumer.springkafkaconsumer.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.service.ElasticBookService;
import com.kafkaconsumer.springkafkaconsumer.service.KafkaConsumer;
import com.kafkaconsumer.springkafkaconsumer.config.ElasticConfig;

@RestController
@RequestMapping("/elastic")
public class ElasticController {

	@Autowired
	private ElasticBookService elasticBookService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticController.class);

	@GetMapping("/getAllBooks")
	public ResponseEntity<List<Books>> getAllBooksDetails() {
		List<Books> allBook;
		try {
			allBook = elasticBookService.getAllBooks();
		} catch (Exception e) {
			LOGGER.error("While retrive getting error \n" + e);
			return new ResponseEntity<List<Books>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Books>>(allBook, HttpStatus.OK);
	}

	@GetMapping("/deleteAllBooks")
	public ResponseEntity<String> deleteAllBooks() {
		try {
			elasticBookService.deleteAllBooks();
		} catch (Exception e) {
			LOGGER.error("While retrive getting error \n" + e);
			return new ResponseEntity<String>("\"While deleting error \\n\" + e", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Deleted all books from Elastic server", HttpStatus.OK);
	}

	@GetMapping("/view/name/{bookname}")
	public List<Books> searchByBookName(@PathVariable final String bookname) {
		List<Books> searchBooks = null;

		searchBooks = elasticBookService.findBookByName(bookname);

		LOGGER.info("search by bookname: " + bookname + "\nSearched list:" + searchBooks);
		return searchBooks;
	}

	@GetMapping("/view/priceCriteria")
	public List<Books> findByPriceCriteria(@RequestParam(required = false, name = "greaterThan") Integer greaterThan,
			@RequestParam(required = false, name = "lessThan") Integer lessThan) {
		
		List<Books> searchBooks = null;

		searchBooks = elasticBookService.findByPriceCriteria(greaterThan, lessThan);

		LOGGER.info("search by Price Criteria between : " + greaterThan+ " and " + lessThan  + "\nSearched list:" + searchBooks);
		return searchBooks;
	}

    @GetMapping("/view/findByNamePrice")
    public List<Books> searchByBookName(@RequestParam(required = false, name = "bookname") String bookname,
			@RequestParam(required = false, name = "price") Integer price) {
    	List<Books> searchBooks = null;
    	
    	searchBooks = elasticBookService.findByNameAndPrice(bookname, price);
    	
        System.out.println("search by findByNamePrice: "  + searchBooks);
        return searchBooks;

    }

}
