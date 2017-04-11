/**
 * 
 */
package com.example;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author gurushant
 *
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/test")

public class RestController {
	
	@Autowired
	DataFetcherService dataServObj;
	
	@RequestMapping(value="processData",
			produces="application/json",
			method=RequestMethod.POST)
	public ResponseEntity<String> processData(@RequestParam String query)
	{	
		query=URLEncoder.encode(query);
		return dataServObj.processData(query);
	}

}
