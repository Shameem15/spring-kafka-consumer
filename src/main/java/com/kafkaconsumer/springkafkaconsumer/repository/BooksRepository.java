package com.kafkaconsumer.springkafkaconsumer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.kafkaconsumer.springkafkaconsumer.model.Books;

@Repository
public interface BooksRepository extends ElasticsearchRepository<Books, Integer> {

//    Page<Books> findByAuthor(String author, Pageable pageable);
//
//    List<Books> findByTitle(String title);

//	@Query("{\"bool\" : {\"must\" : [ {\"field\" : {\"bookname\" : \"?0\"}}, {\"field\" : {\"price\" : \"?0\"}} ]}}")
//	List<Books> findByNameAndPrice(String bookname, Integer price);
}
