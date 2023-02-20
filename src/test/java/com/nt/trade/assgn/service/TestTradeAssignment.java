package com.nt.trade.assgn.service;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.nt.trade.assgn.store.Trade;
import com.nt.trade.assgn.store.TradeStore;

/**
 * An integration test class that relies on JUnit 5 to run unit test cases
 * 
 * @author Narendra Thite
 *
 */

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class TestTradeAssignment {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@LocalServerPort
	private int rPort;

	@Autowired
	private TradeStore tradeStore;
	
	@Autowired
	private TradeObjectMapper mapper;
	
	/**
	 * A helper method which takes Trade Json array as an argument and returns REST response object.
	 * 
	 * @param tradeArray
	 * @return
	 * @throws Exception
	 */
	private ResponseEntity<String> executeEndpoint(String tradeArray) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		
		String endPoint = "http://localhost:" + rPort + "/trades";
		
		URI uri = new URI(endPoint);
		
		HttpHeaders headers = new HttpHeaders();

		HttpEntity<String> request = new HttpEntity<String>(tradeArray, headers);
		
		ResponseEntity<String> res = restTemplate.postForEntity(uri, request, String.class);
		
		return res;
	}
	
	
	/**
	 * An unit test case to check if a valid trade object is stored in Trade-Store or not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddTradeSuccessfully() throws Exception {
		
		String tradeArray = "{\"tradeId\":\"T0\", \"version\":\"1\", \"bookId\":\"B1\", \"counterPartyId\":\"CP-1\", "
				+ "\"maturityDate\":\"2023-02-18\"}";
		
		ResponseEntity<String> res = executeEndpoint(tradeArray);
		
		Assertions.assertEquals(200, res.getStatusCodeValue());
		
		Assertions.assertEquals("TradeExecuted: Successfully", res.getBody());
		
		Trade trade = mapper.convertToTrade(tradeArray);
		
		Set<Trade> set = tradeStore.getTrades(trade);
		
		for (Trade trade2 : set) {
			if (trade.getVersion().equals(trade.getVersion())) {
				Assertions.assertEquals("B1", trade2.getBookId());
				Assertions.assertEquals("CP-1", trade2.getCounterPartyId());
				break;
			}
		}
		
	}

	/**
	 * This method checks whether application can successfully handles the lower trade version validation rule
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLowerVersionReceived() throws Exception {
		
		
		String tradeArray1 = "{\"tradeId\":\"T1\", \"version\":\"2\", \"bookId\":\"B2\", \"counterPartyId\":\"CP-2\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray1);

		String tradeArray2 = "{\"tradeId\":\"T1\", \"version\":\"3\", \"bookId\":\"B3\", \"counterPartyId\":\"CP-3\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray2);

		String tradeArray3 = "{\"tradeId\":\"T1\", \"version\":\"1\", \"bookId\":\"B1\", \"counterPartyId\":\"CP-1\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		ResponseEntity<String> res = executeEndpoint(tradeArray3);

		Assertions.assertEquals(200, res.getStatusCodeValue());
		
		Assertions.assertEquals("TradeExecutionFailed: LowerVersionReceived", res.getBody());
	}
	
	/**
	 * This method checks whether application can successfully handles the older maturity validation rule
	 * 
	 * @throws Exception
	 */
	@Test
	public void testOlderMaturityDate() throws Exception {
		
		String tradeArray1 = "{\"tradeId\":\"T2\", \"version\":\"2\", \"bookId\":\"B2\", \"counterPartyId\":\"CP-2\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray1);

		String tradeArray2 = "{\"tradeId\":\"T2\", \"version\":\"3\", \"bookId\":\"B3\", \"counterPartyId\":\"CP-3\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray2);

		String tradeArray3 = "{\"tradeId\":\"T2\", \"version\":\"4\", \"bookId\":\"B1\", \"counterPartyId\":\"CP-1\", "
				+ "\"maturityDate\":\""+ getMaturityDateForTest(-1)  + "\"}";
		
		ResponseEntity<String> res = executeEndpoint(tradeArray3);

		Assertions.assertEquals(200, res.getStatusCodeValue());
		
		Assertions.assertEquals("TradeExecutionFailed: OlderMaturityDate", res.getBody());
	}
	
	/**
	 * This method tests whether application can replace the old trade with new trade when their trade-id and version are same
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSameVersionReceived() throws Exception {
		
		String tradeArray1 = "{\"tradeId\":\"T3\", \"version\":\"2\", \"bookId\":\"B2\", \"counterPartyId\":\"CP-2\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray1);

		String tradeArray2 = "{\"tradeId\":\"T3\", \"version\":\"3\", \"bookId\":\"B3\", \"counterPartyId\":\"CP-3\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray2);

		String tradeArray3 = "{\"tradeId\":\"T3\", \"version\":\"2\", \"bookId\":\"B1\", \"counterPartyId\":\"CP-1\", "
				+ "\"maturityDate\":\""+ getMaturityDateForTest(1)  + "\"}";
		
		ResponseEntity<String> res = executeEndpoint(tradeArray3);

		Assertions.assertEquals(200, res.getStatusCodeValue());
		
		Assertions.assertEquals("TradeExecuted: replaced old trade", res.getBody());
		
		Trade trade = mapper.convertToTrade(tradeArray3);
		
		Set<Trade> set = tradeStore.getTrades(trade);
		
		for (Trade trade2 : set) {
			if (trade.getVersion().equals(trade.getVersion())) {
				Assertions.assertEquals("B1", trade2.getBookId());
				break;
			}
		}
	}

	/**
	 * This method tests the working of Trade expiry check scheduler.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTradeExpiryCheckSchedule() throws Exception {
		
		String tradeArray1 = "{\"tradeId\":\"T4\", \"version\":\"1\", \"bookId\":\"B2\", \"counterPartyId\":\"CP-2\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray1);

		ResponseEntity<String> res = executeEndpoint(tradeArray1);

		Assertions.assertEquals(200, res.getStatusCodeValue());

		
		Trade trade = mapper.convertToTrade(tradeArray1);
		
		Set<Trade> set = tradeStore.getTrades(trade);
		
		for (Trade trade2 : set) {
			if (trade.getVersion().equals(trade.getVersion())) {
				trade2.setMaturityDate(getMaturityDateObjetForTest(-1));
				break;
			}
		}
		
		Thread.sleep(6000);

		for (Trade trade2 : set) {
			if (trade.getVersion().equals(trade.getVersion())) {
				Assertions.assertEquals("Y", trade2.getExpired());
				break;
			}
		}
		
	}
	
	/**
	 * This method tests whether Json structural validation is handled or not
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIncorrectTradeJSONArray() throws Exception {
		
		String tradeArray1 = "{\"tradeId\":\"T5\", \"version\":\"KK\", \"bookId\":\"B2\", \"counterPartyId\":\"CP-2\", "
				+ "\"maturityDate\":\"" + getMaturityDateForTest(1)  + "\"}";
		
		executeEndpoint(tradeArray1);

		ResponseEntity<String> res = executeEndpoint(tradeArray1);

		Assertions.assertEquals(200, res.getStatusCodeValue());

		Assertions.assertEquals("TradeExecutionFailed: Failed to Parse Trade JSON string", res.getBody());

	}

	/**
	 * A helper method that constructs a date in 'yyyy-MM-dd' format, it uses the integer argument to add/substract days from current date
	 * 
	 * @param change
	 * @return
	 */
	private String getMaturityDateForTest(int change) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, change);
		
		return sdf.format(cal.getTime());
		
	}

	/**
	 * A helper method that constructs a java date object, it uses the integer argument to add/substract days from current date
	 * 
	 * @param change
	 * @return
	 */
	private Date getMaturityDateObjetForTest(int change) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, change);
		
		return cal.getTime();
		
	}
}
