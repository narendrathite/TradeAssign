package com.nt.trade.assgn.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.trade.assgn.store.Trade;

/**
 * This service is responsible for executing all the validation rules. 
 * A new validation rules can be added into a class that 
 * implements <code>TradeValidation</code> interface and the <code>validateTrade</code> method can be called from here.
 * 
 * @author Narendra Thite
 *
 */

@Service
public class TradeValidationService {

	@Autowired
	private MaturityDateValidator mdValidator;

	@Autowired
	private VersionOfTradeValidator vValidator;

	/**
	 * This method runs all the validation rules and populates the TradeValidationResult in case if any validation erros are found.
	 * 
	 * @param trade - Trade Object
	 * @return - TradeValidationResult object
	 */
	public TradeValidationResult validateTrade(Trade trade) {
		TradeValidationResult tvResult = new TradeValidationResult();
		
		vValidator.validateTrade(trade, tvResult);
		mdValidator.validateTrade(trade, tvResult);
		
		return tvResult;
	}
}
