package com.kafkaconsumer.springkafkaconsumer.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "kafka-spring-producer", type = "books")
//@Document(indexName = "kafka-spring-producer", type = "books" )
public class Books {

	@Id
	@Field(type = FieldType.Keyword)
	private int bookid;
	
	@Field(type = FieldType.Text)
	private String bookname;
	
	@Field(type = FieldType.Text)
	private String author;

	@Field(type = FieldType.Integer)
	private int price;
	
	@Field(type = FieldType.Nested, includeInParent = true)
	private List<Books> books;
	
	public int getBookid() {
		return bookid;
	}

	public void setBookid(int bookid) {
		this.bookid = bookid;
	}

	public String getBookname() {
		return bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Books [bookid=");
		builder.append(bookid);
		builder.append(", bookname=");
		builder.append(bookname);
		builder.append(", author=");
		builder.append(author);
		builder.append(", price=");
		builder.append(price);
		builder.append("]");
		return builder.toString();
	}

}