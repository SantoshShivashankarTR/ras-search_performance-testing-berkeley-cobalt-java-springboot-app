package com.trgr.berkeleydbtestingapplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.trgr.berkeleydb")
public class ServerRunner {

	public static void main(String[] args) {

		SpringApplication.run(ServerRunner.class, args);
	}

}
