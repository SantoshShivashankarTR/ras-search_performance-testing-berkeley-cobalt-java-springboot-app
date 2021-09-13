package com.trgr.berkeleydbtestingapplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.trgr.berkeleydb")
public class ServerRunner {
	private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
	    Properties p = new Properties();
		p.load(getFileFromResourceAsStream()); // Load the properties from a file in your jar
	    for (String name : p.stringPropertyNames()) {
	        String value = p.getProperty(name);
	        System.setProperty(name, value);
	    }
		
		log.info("Start Berkeley testing app");
		SpringApplication.run(ServerRunner.class, args);
	}

	
	private static InputStream getFileFromResourceAsStream() {

		// The class loader that loaded the class
		ClassLoader classLoader = ServerRunner.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream("application.properties");
		// the stream holding the file content
		return inputStream;
	}
}
