package com.nt.trade.assgn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.nt.trade.assgn.store.Trade;
import com.nt.trade.assgn.store.TradeStore;
import com.nt.trade.assgn.validate.TradeNonRecoverableException;
import com.nt.trade.assgn.validate.TradeValidationEnum;
import com.nt.trade.assgn.validate.TradeValidationResult;
import com.nt.trade.assgn.validate.TradeValidationService;

/**
 * This a Spring Boot enabled service class which is responsible for validating an incoming trade and 
 * storing it in trade-store (only if trade passes all the validations)
 * 
 * @author Narendra Thite
 *
 */

@Service
public class TradeService {

	private static final Logger logger = LogManager.getLogger(TradeService.class);
	
	@Autowired
	private TradeValidationService tvService;
	
	@Autowired
	private TradeObjectMapper mapper;
	
	@Autowired
	private TradeStore tradeStore;	
	
	/**
	 * This method converts the Json string to Trade Object. After conversion it calls TradeValidationService to run all the validation rules
	 * It stores the Trade object into trade-store post successful validation
	 * 
	 * @param tradeJSONArray - a JSON string containing trade details in format as - 
	 * {"tradeId":"T1", "version":"1", "bookId":"B1", "counterPartyId":"CP-1", "maturityDate":"2021-04-12"} 
	 * the date is expected in yyyy-MM-dd format
	 * 
	 * @return a status indicating whether addition of trade is successful or not
	 */
	public String addTrade(String tradeJSONArray) {
		String result = "TradeExecuted";
		String reason = "Successfully";
		try {
			
			Trade trade = mapper.convertToTrade(tradeJSONArray);
			trade.setCreatedDate(new Date());

			TradeValidationResult tvResult = tvService.validateTrade(trade);
			
			boolean isTradeValid = handleValidationResults(trade, tvResult);
			
			if (isTradeValid) {
				
				tradeStore.addToStore(trade);
			}
			else {
				logger.info("Old trade has been replaced with new for tradeId: " + trade.getTradeId());
				reason = "replaced old trade";
			}
		} catch (TradeNonRecoverableException ex) {
			
			reason = ex.getReason();
			result = "TradeExecutionFailed";
		}
		
		return result + ": " + reason;
	}

	/**
	 * This method receives a Trade Id and returns matching trades from the trade store. 
	 * 
	 * @param tradeId -  trade id  
	 * 
	 * @return a list of matching trades from trade store
	 */
	public List<Trade> getTrade(String tradeId) {

		List<Trade> tradeList = new ArrayList<>();
		
		
		if (!StringUtils.hasText(tradeId) || "all".equalsIgnoreCase(tradeId)) {
			
			tradeList.addAll(tradeStore.getAllTrades());
			
		} else {

			tradeList.addAll(tradeStore.getTradesById(tradeId));
		}
		
		return tradeList;
	}
	
	
	/**
	 * This method converts TradeValidationResults into Exception. It also replaces old trade with new trade if their versions are same.
	 * 
	 * @param trade - Trade Object
	 * @param tvResult - trade validation result
	 * @return - boolean indicating whether trade validation is successful or not
	 * 
	 * @throws TradeNonRecoverableException - if trade validations are not met
	 */
	private boolean handleValidationResults(Trade trade, TradeValidationResult tvResult) throws TradeNonRecoverableException {
		
		List<TradeValidationEnum> list = tvResult.getMessages();
		
		for (TradeValidationEnum tradeValidationEnum : list) {
			
			if (tradeValidationEnum.equals(TradeValidationEnum.LowerVersionReceived)) {
				
				logger.error(TradeValidationEnum.LowerVersionReceived.getErrorDescription(new Object[] {trade.getTradeId()}));
				
				throw new TradeNonRecoverableException(TradeValidationEnum.LowerVersionReceived.name(), 
						TradeValidationEnum.LowerVersionReceived.getErrorDescription(new Object[] {trade.getTradeId()}));
			}
			
			if (tradeValidationEnum.equals(TradeValidationEnum.SameVersionReceived)) {
				
				Set<Trade> trades = tradeStore.getTrades(trade);
				
				for (Trade existingTrade : trades) {
					
					if (existingTrade.getVersion().equals(trade.getVersion())) {
						trades.remove(existingTrade);
						trades.add(trade);
						return false;
					}
				}
				
			}
			
			if (tradeValidationEnum.equals(TradeValidationEnum.OlderMaturityDate)) {
				
				logger.error(TradeValidationEnum.OlderMaturityDate.getErrorDescription(new Object[] {trade.getTradeId()}));
				
				throw new TradeNonRecoverableException(TradeValidationEnum.OlderMaturityDate.name(), 
						TradeValidationEnum.OlderMaturityDate.getErrorDescription(new Object[] {trade.getTradeId()}));
			}
			
		}
		
		return true;
	}

}
