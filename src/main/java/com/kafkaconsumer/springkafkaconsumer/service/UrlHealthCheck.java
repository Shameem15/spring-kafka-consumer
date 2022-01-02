package com.kafkaconsumer.springkafkaconsumer.service;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class UrlHealthCheck implements HealthIndicator {

	private String url;
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlHealthCheck.class);

	@Override
	public Health health() {
		// check if url shortener service url is reachable
		try (Socket socket = new Socket(new java.net.URL(url).getHost(), new java.net.URL(url).getPort())) {
		} catch (Exception e) {
			LOGGER.info("Failed to connect to: " + url);
			return Health.down().withDetail("error", e.getMessage()).build();
		}
		return Health.up().build();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
