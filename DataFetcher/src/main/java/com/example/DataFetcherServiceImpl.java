/**
 * 
 */
package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.json.JSONException;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * @author gurushant
 *
 */
@Service(value="dataFetcherServiceImpl")
public class DataFetcherServiceImpl implements DataFetcherService {




	@Override
	public ResponseEntity<String> processData(String serachText) {
		// TODO Auto-generated method stub


		RestTemplate restSearchApiObj=new RestTemplate();
		String urlSearchApi=Constant.SEARCH_API+"="+serachText+"&"
				+ "key="+Constant.SEARCH_API_KEY;
		List<DataSheet>listData=new ArrayList<>();
		while(true)
		{
			ResponseEntity<String>respSearchEntity=restSearchApiObj.postForEntity(urlSearchApi, null, String.class);
			Gson gsonObj=new Gson();
			Map<String, Object>mapResponse=gsonObj.fromJson(respSearchEntity.getBody(), Map.class);
			Iterator<Entry<String, Object>>mapIter= mapResponse.entrySet().iterator();
			String nextPageToken=mapResponse.get("next_page_token")==null?null:mapResponse.get("next_page_token").toString();

			List<String> list=(ArrayList<String>)mapResponse.get("html_attributions");

			List<Object>listResult=(ArrayList<Object>)mapResponse.get("results");

			for(int i=0;i<listResult.size();i++)
			{
				DataSheet dataSheetObj=new DataSheet();
				Object item=listResult.get(i);
				LinkedTreeMap<String, Object>linkItemMap=(LinkedTreeMap<String, Object>)item;
				String formattedAddress=linkItemMap.get("formatted_address").toString();
				String name=linkItemMap.get("name").toString();
				String placeId=linkItemMap.get("place_id").toString();
				String rating=linkItemMap.get("rating")==null?null:linkItemMap.get("rating").toString();
				//Fetch place details
				RestTemplate restSearchApiDetailObj=new RestTemplate();
				String urlSearchDetailApi=Constant.SEARCH_DETAIL_API+placeId+"&"
						+ "key="+Constant.SEARCH_API_KEY;
				ResponseEntity<String>respSearchDetailEntity=restSearchApiDetailObj.postForEntity(urlSearchDetailApi, null, String.class);
				//Fetch api details
				Map<String, Object>mapApiDetailResponse=gsonObj.fromJson(respSearchDetailEntity.getBody(), Map.class);
				LinkedTreeMap<String, Object>linkItemDetailMap=(LinkedTreeMap<String, Object>)mapApiDetailResponse.get("result");
				String contactNumber=linkItemDetailMap.get("international_phone_number")==null?null:linkItemDetailMap.get("international_phone_number").toString();

				String website=linkItemDetailMap.get("website")==null?null:linkItemDetailMap.get("website").toString();
				dataSheetObj.setAddress(formattedAddress);
				dataSheetObj.setContactNumber(contactNumber);
				dataSheetObj.setEmailId(null);
				dataSheetObj.setName(name);
				dataSheetObj.setRank(i);
				dataSheetObj.setRating(rating);
				dataSheetObj.setSearchText(serachText);
				dataSheetObj.setWebsiteUrl(website);
				listData.add(dataSheetObj);
			}
			if(nextPageToken==null)
			{
				break;
			}
			else
			{
				urlSearchApi=Constant.SEARCH_API+"=restaurants+in+pune&"
						+ "key="+Constant.SEARCH_API_KEY+"&pageToken="+nextPageToken;
			}			
			if(listData.size() > 400)
			{
				break;
			}
			System.out.println(listData.size());
		}	
		//write data to excel file
		String fileName=""+System.currentTimeMillis();
		File s=new File("data/sample.xlsx");
		File d=new File("data/"+fileName+".xlsx");
		try {
			XSSFWorkbook workbook1 = new XSSFWorkbook(); 
			//Create a blank sheet
			XSSFSheet spreadsheet = workbook1.createSheet( 
					" Employee Info ");

			Row row= spreadsheet.createRow(0);
			row.createCell(0).setCellValue("Sr.no");
			row.createCell(1).setCellValue("Search");
			row.createCell(2).setCellValue("Restaurant");
			row.createCell(3).setCellValue("Rank");
			row.createCell(4).setCellValue("Address");
			row.createCell(5).setCellValue("Contact Number");
			row.createCell(6).setCellValue("Website");
			row.createCell(7).setCellValue("Star Ratings");
			row.createCell(8).setCellValue("Number of Reviews");

			FileOutputStream out = new FileOutputStream( 
					new File("data/"+fileName+".xlsx"));

			//writing data
			for(int l=0;l<listData.size();l++)
			{
				DataSheet ds=listData.get(l);
				row= spreadsheet.createRow(l+1);	   
				row.createCell(0).setCellValue(l+1);
				row.createCell(1).setCellValue(serachText);
				row.createCell(2).setCellValue(ds.getName());
				row.createCell(3).setCellValue(l+1);
				row.createCell(4).setCellValue(ds.getAddress());
				row.createCell(5).setCellValue(ds.getContactNumber());
				row.createCell(6).setCellValue(ds.getWebsiteUrl());
				row.createCell(7).setCellValue(ds.getRating());
				row.createCell(8).setCellValue("a");

			}

			workbook1.write(out);
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

