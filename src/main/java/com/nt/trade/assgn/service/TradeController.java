package com.nt.trade.assgn.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.trade.assgn.store.Trade;

/**
 * Rest controller that handles the /trades endpoint. This controller is responsible for adding a trade to a trade store
 * 
 * @author Narendra Thite
 *
 */

@RestController
@RequestMapping(path = "/trades")
public class TradeController {

	private static final Logger logger = LogManager.getLogger(TradeController.class);
	
	@Autowired
	private TradeService tradeService;
	
	/**
	 * This method handles a POST request to create or store trade in trade-store. It uses <code>TradeService</code> to validate and store the trade.
	 * 
	 * @param tradeJSONArray - a JSON string containing trade details in format as - 
	 * {"tradeId":"T1", "version":"1", "bookId":"B1", "counterPartyId":"CP-1", "maturityDate":"2021-04-12"} 
	 * the date is expected in yyyy-MM-dd format
	 * 
	 * @return a status indicating whether addition of trade is successful or not
	 */
	
	@PostMapping
	public String executeTrade(@RequestBody String tradeJSONArray) {
		
		String result = tradeService.addTrade(tradeJSONArray);
		
		logger.info("Result of trade add for a trade " + tradeJSONArray + " is: " + result);
		
		return result;
	}
	
	/**
	 * This method receives a Trade Id and returns matching trades from the trade store. 
	 * 
	 * @param tradeId -  trade id  
	 * 
	 * @return a list of matching trades from trade store
	 */
	
	@GetMapping
	public String getTrade(@RequestParam(name = "tradeId") String tradeId) {
		
		String result = null;
		
		try {
		
			List<Trade> tradeList = tradeService.getTrade(tradeId);
		
			ObjectMapper om = new ObjectMapper();
		
			result = om.writeValueAsString(tradeList);
		
			logger.info("Result of view trade  " + result);
			
		} catch (JsonProcessingException jpe) { 
			result = "";
			logger.error("Failed to find a matching trade for given trade id: " + tradeId);
		}
			
		return result;
	}	
}
