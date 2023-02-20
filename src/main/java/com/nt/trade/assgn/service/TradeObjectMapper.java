package com.nt.trade.assgn.service;

import org.springframework.stereotype.Service;

import com.nt.trade.assgn.store.Trade;
import com.nt.trade.assgn.validate.TradeNonRecoverableException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class uses Jackson libraries object mapper to conver a JSON array into a Java Object
 * 
 * @author Narendra Thite
 *
 */

@Service
public class TradeObjectMapper {

	/**
	 * 
	 * @param tradeJSONArray - a JSON string containing trade details in format as - 
	 * {"tradeId":"T1", "version":"1", "bookId":"B1", "counterPartyId":"CP-1", "maturityDate":"2021-04-12"} 
	 * the date is expected in yyyy-MM-dd format
	 * 
	 * @return - an Object of type com.nt.trade.assgn.Store.Trade
	 * 
	 * @throws TradeNonRecoverableException - wraps the Json mapping or parsing exception 
	 * thrown by Jackson library if Json string is not in expected format
	 * 
	 */
	public Trade convertToTrade(String tradeJSONArray) throws TradeNonRecoverableException {
		Trade trade = null;
		
		if (tradeJSONArray == null) {
			return null;
		}
		
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			
			trade = mapper.readValue(tradeJSONArray, Trade.class);
			
		} catch (JsonMappingException jme) {
			jme.printStackTrace();
			throw new TradeNonRecoverableException("Failed to Parse Trade JSON string", "Unable to map incoming trade string to trade object: " + tradeJSONArray);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
			throw new TradeNonRecoverableException("Failed to Parse Trade JSON string", "Unable to process trade string: " + tradeJSONArray);
		}
		
		return trade;
	}
}
