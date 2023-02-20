package com.nt.trade.assgn.validate;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.trade.assgn.store.Trade;
import com.nt.trade.assgn.store.TradeStore;

/**
 * This class validates the version of a trade
 * 
 * @author Narendra Thite
 *
 */
@Service
public class VersionOfTradeValidator implements TradeValidator {

	@Autowired
	private TradeStore tradeStore;	
	
	/**
	 * This method validates the version of a trade. 
	 * If version of a trade is lesser than currently available versions then it adds an error to the TradeValidationResult object.
	 */	
	@Override
	public void validateTrade(Trade trade, TradeValidationResult result) {

		if (trade == null || result == null) {
			return;
		}
		
		Set<Trade> set = tradeStore.getTrades(trade);
		
		if (set != null && !set.isEmpty()) {
		
			if (set.iterator().next().getVersion() > trade.getVersion()) {
				result.addValidationResult(TradeValidationEnum.LowerVersionReceived);
			}
			else {
				for (Trade t : set) {
					if (t.getVersion().equals(trade.getVersion())) {
						result.addValidationResult(TradeValidationEnum.SameVersionReceived);
					}
				}
			}
		}
		
	}
}
