package com.nt.trade.assgn.validate;

/**
 * An exception class. This exception will thrown when an incoming trade has any validation error or the trade Json string is not well formed
 * 
 * @author Narendra Thite
 *
 */
public class TradeNonRecoverableException extends Exception {

	private String reason;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2279136578903207425L;

	public TradeNonRecoverableException() {
		super();
	}
	
	public TradeNonRecoverableException(String reason, String errorDesc) {
		super(errorDesc);
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	
}
