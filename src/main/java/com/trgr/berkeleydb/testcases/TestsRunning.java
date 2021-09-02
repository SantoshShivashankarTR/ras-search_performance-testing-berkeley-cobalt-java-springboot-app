package com.trgr.berkeleydb.testcases;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trgr.berkeleydb.contoller.BerkeleyDBController;
import com.trgr.berkeleydb.testcases.data.Result;

public class TestsRunning {
	private static final Logger log = LoggerFactory.getLogger(BerkeleyDBController.class);
	
	private DBRepository<String, Result> dbRepository;
	
	public void startTests() {
		
		Set<String> keys = new HashSet<>();
		Map<String, Result> res = dbRepository.getSValues(keys);

		// Result for Cases
		log.info("Result: " + res);
	}

	public void setDbRepository(DBRepository<String, Result> dbRepository) {
		this.dbRepository = dbRepository;
	}
}