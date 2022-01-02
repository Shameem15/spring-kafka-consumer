package com.kafkaconsumer.springkafkaconsumer.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.kafkaconsumer.springkafkaconsumer.model.Books;
import com.kafkaconsumer.springkafkaconsumer.repository.BooksRepository;

@Service
public class ElasticBookService {

	private static final String BOOKS_INDEX = "kafka-spring-producer";

	@Autowired
	private UrlHealthCheck urlHealthCheck;

	@Autowired
	private BooksRepository booksRepository;

	@Value("${elasticsearch.servers}")
	private String elasticServer;

	private static final String HTTP = "http://";

	@Autowired
	private ElasticsearchOperations elasticsearchTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticBookService.class);

	public List<Books> getAllBooks() throws Exception {
		List<Books> books = new ArrayList<Books>();

		urlHealthCheck.setUrl(HTTP + elasticServer);
		Health health = urlHealthCheck.getHealth(false);
		if (health.getStatus().getCode().equals("UP")) {

			booksRepository.findAll().forEach(books1 -> books.add(books1));
			LOGGER.info("save list of books: \n" + books);
		} else {
			LOGGER.error("Elastic Server is not UP. So, we can't get list of books");
			throw new Exception("Elastic Server is not UP. So, we can't get list of books");    
		}
		return books;
	}

	public void deleteAllBooks() {
		booksRepository.deleteAll();
	}

	public List<Books> findBookByName(final String bookname) {

		// 1. Create query
		QueryBuilder queryBuilder = QueryBuilders.matchQuery("bookname", bookname);

		Query searchQuery = new StringQuery("{\"match\":{\"bookname\":{\"query\":\"" + bookname + "\"}}}\"");
		// Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

		// 2. Execute search
		SearchHits<Books> booksHits = elasticsearchTemplate.search(searchQuery, Books.class,
				IndexCoordinates.of(BOOKS_INDEX));

		// 3. Map searchHits to books list
		List<Books> booksMatches = new ArrayList<Books>();
		booksHits.forEach(searchHit -> {
			booksMatches.add(searchHit.getContent());
		});

		return booksMatches;
	}

	public List<Books> findByPriceCriteria(Integer greaterThan, Integer lessThan) {
		Criteria criteria = new Criteria("price").greaterThan(greaterThan).lessThan(lessThan);

		Query searchQuery = new CriteriaQuery(criteria);

		SearchHits<Books> booksHits = elasticsearchTemplate.search(searchQuery, Books.class,
				IndexCoordinates.of(BOOKS_INDEX));

		List<Books> booksMatches = new ArrayList<Books>();
		booksHits.forEach(searchHit -> {
			booksMatches.add(searchHit.getContent());
		});
		return booksMatches;
	}

	public List<Books> findByNameAndPrice(String bookname, Integer price) {
		
		Criteria criteria = new Criteria("bookname").is(bookname).and("price").is(price);

		Query searchQuery = new CriteriaQuery(criteria);
		
//		Query searchQuery = new NativeSearchQueryBuilder()
//			    .withQuery(QueryBuilders.matchQuery("bookname", bookname)).
//			    .withQuery(QueryBuilders.matchQuery("price", price))
//			    .build();

		SearchHits<Books> booksHits = elasticsearchTemplate.search(searchQuery, Books.class,
				IndexCoordinates.of(BOOKS_INDEX));

		List<Books> booksMatches = new ArrayList<Books>();
		booksHits.forEach(searchHit -> {
			booksMatches.add(searchHit.getContent());
		});

		return booksMatches;
	}

}
