package com.trgr.berkeleydb.contoller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trgr.berkeleydb.config.DefaultBerkeleyStateBlockManagerImpl;
import com.trgr.berkeleydb.entity.BerkeleyEntries;
import com.trgr.berkeleydb.service.BerkeleyAppService;
import com.trgr.berkeleydb.testcases.TestsRunning;

@RestController
public class BerkeleyDBController {
	private static final Logger log = LoggerFactory.getLogger(BerkeleyDBController.class);
	
	//private final TestsRunning testsRunning;
	/*
	 * @Autowired public TestingDBController(TestsRunning testsRunning) {
	 * this.testsRunning = testsRunning; }
	 * 
	 * @GetMapping(value = "/runTests", produces = MediaType.APPLICATION_JSON_VALUE)
	 * public Map<String, String> runTests() { log.info("Start Berkeley testing");
	 * 
	 * testsRunning.startTests();
	 * 
	 * Map<String, String> res = new HashMap<>(); res.put("status", "OK");
	 * 
	 * return res; }
	 */
	
	  @Autowired
	    private BerkeleyAppService berkeleyService;
	  
	    @RequestMapping(method = RequestMethod.GET, path = "/bdb/{db}",
	        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    @ResponseBody
	    public ResponseEntity<String> listEntries(
	        @PathVariable("db") final String database,
	        @RequestParam(name = "limit", defaultValue = "10") final int limit) throws Exception
	    {
	    	final BerkeleyEntries response = new BerkeleyEntries();
	    	  response.setDatabase(database);
	          response.setEntries(berkeleyService.listEntries(database, limit > 1 ? 10 : limit));
	    	
	    	
	        final ObjectMapper mapper = new ObjectMapper()
	            .enable(SerializationFeature.INDENT_OUTPUT)
	            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	        return ResponseEntity.ok(mapper.writeValueAsString(response));
	    }
	    
	    @RequestMapping(method = RequestMethod.GET, path = "/bdb/testing100K",
		        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		    @ResponseBody
		    public ResponseEntity<String> listEntries() throws Exception
		    {
		    	final BerkeleyEntries response = new BerkeleyEntries();
		    	
		         response.setTimes((berkeleyService.listEntriesTesting()));
		    	
		    	
		        final ObjectMapper mapper = new ObjectMapper()
		            .enable(SerializationFeature.INDENT_OUTPUT)
		            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	        
		        return ResponseEntity.ok(mapper.writeValueAsString(response));
		    }
	    

	    @RequestMapping(method = RequestMethod.GET, path = "/bdb",
	        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    @ResponseBody
	    public ResponseEntity<String> listDatabases() throws JsonProcessingException
	    {
	        final List<String> response = berkeleyService.listDatabases();
	        final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	        return ResponseEntity.ok(mapper.writeValueAsString(response));
	    }
}
