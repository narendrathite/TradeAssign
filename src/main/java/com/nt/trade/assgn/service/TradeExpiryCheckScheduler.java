package com.nt.trade.assgn.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nt.trade.assgn.store.Trade;
import com.nt.trade.assgn.store.TradeStore;

/**
 * 
 * This is a Spring Boot scheduler which runs periodically to check the trade expiry
 * 
 * @author Narendra Thite
 *
 */

@Component
public class TradeExpiryCheckScheduler {

	private static final Logger logger = LogManager.getLogger(TradeExpiryCheckScheduler.class);
	
	@Autowired
	private TradeStore tradeStore;
	
	/**
	 * This method is executed after every 5 seconds by the Spring Scheduler. 
	 * It iterates all the trade available in trade-store and sets expiry of those trade which are older than current date/
	 * 
	 */
	
	@Scheduled(fixedRate = 5000)
	public void checkExpiry() {
		
		Date today = new Date();
		
		Set<Trade> trades = tradeStore.getAllTrades();
		
		for (Trade trade : trades) {
			
			if (getDateWithoutTime(trade.getMaturityDate()).before(getDateWithoutTime(today))) {
				
				logger.info("Trade: " + trade.getTradeId() + " has been expired");
				
				trade.setExpired("Y");
			}
		}
	}
	
	/**
	 * private method that returns a date after stripping the time values
	 * 
	 * @param date
	 * @return a date after stripping the time values
	 */
	private static Date getDateWithoutTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}	
}
