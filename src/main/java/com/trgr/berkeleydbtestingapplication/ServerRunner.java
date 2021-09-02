package com.trgr.berkeleydbtestingapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.trgr.berkeleydb")
public class ServerRunner {
	private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);

	public static void main(String[] args) {
		log.info("Start Berkeley testing app");
		SpringApplication.run(ServerRunner.class, args);
	}

}
