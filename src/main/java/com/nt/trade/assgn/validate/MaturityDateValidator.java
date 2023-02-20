package com.nt.trade.assgn.validate;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nt.trade.assgn.store.Trade;

/**
 * 
 * This class validates maturity date of trade
 * 
 * @author Narendra Thite
 *
 */
@Service
public class MaturityDateValidator implements TradeValidator {

	/**
	 * This method validates the Maturity date of a trade. 
	 * If maturity date is older than current date then it adds OlderMaturityDate error to the TradeValidationResult object.
	 */
	@Override
	public void validateTrade(Trade trade, TradeValidationResult result) {

		if (trade == null || result == null) {
			return;
		}
		
		if (getDateWithoutTime(trade.getMaturityDate()).before(getDateWithoutTime (new Date()))) {
			
			result.addValidationResult(TradeValidationEnum.OlderMaturityDate);
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
