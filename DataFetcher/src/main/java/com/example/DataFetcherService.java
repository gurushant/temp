/**
 * 
 */
package com.example;

import org.springframework.http.ResponseEntity;

/**
 * @author gurushant
 *
 */

public interface DataFetcherService {
public ResponseEntity<String> processData(String serachText);
}
