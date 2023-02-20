package com.nt.trade.assgn.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores the validation results.
 * 
 * @author Narendra Thite
 *
 */
public class TradeValidationResult {

	private List<TradeValidationEnum> messages = new ArrayList<>();

	/**
	 * This method adds the TradeValidationEnum into list
	 * 
	 * @param tvEnum - TradeValidationEnum
	 */
	public void addValidationResult(TradeValidationEnum tvEnum) {
		messages.add(tvEnum);
	}

	/**
	 * 
	 * @return list of TradeValidationEnum
	 */
	public List<TradeValidationEnum> getMessages() {
		return messages;
	}
	
	
}
