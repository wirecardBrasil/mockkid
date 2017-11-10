package br.com.moip.mockkid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MockkidApplication {

	private static final Logger logger = LoggerFactory.getLogger(MockkidApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MockkidApplication.class, args);
	}

}
