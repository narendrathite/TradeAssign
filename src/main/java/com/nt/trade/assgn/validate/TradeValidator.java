package com.nt.trade.assgn.validate;

import com.nt.trade.assgn.store.Trade;

/**
 * A trade validation interface
 * 
 * @author Narendra Thite
 *
 */
public interface TradeValidator {

	/**
	 * The implementing classes will override this method to handle a specific validation condition
	 * 
	 * @param trade - Trade Object
	 * @param result - TradeValidationResult containing validation errors
	 */
	public void validateTrade(Trade trade, TradeValidationResult result);
}
