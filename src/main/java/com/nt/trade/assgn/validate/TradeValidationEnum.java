package com.nt.trade.assgn.validate;

import java.text.MessageFormat;

/**
 * An enumeration that list the validation errors.
 * 
 * @author Narendra Thite
 *
 */
public enum TradeValidationEnum {

	LowerVersionReceived("LowerVersionReceived", "Lower version of Trade received for trade id {0}"),
	SameVersionReceived("SameVersionReceived", "Same version of Trade received for trade id {0}"),
	OlderMaturityDate("OlderMaturityDate", "Maturity Date is less than today date for trade id {0}");
	
	TradeValidationEnum(String errorCode, String errorDescription) {
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
	}
	
	private String errorCode;
	private String errorDescription;
	
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	
	public String getErrorDescription(Object[] args) {
		String errorDesc = errorDescription;
		
		if (args != null && args.length > 0) {
			errorDesc = MessageFormat.format(errorDescription, args);
		}
		
		return errorCode + ": " + errorDesc;
	}
}
