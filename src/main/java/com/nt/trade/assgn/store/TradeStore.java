package com.nt.trade.assgn.store;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * A Trade Store service which relies on concurrent hash map to hold the trades. 
 * It uses TradeId as a key to store multiple trade objects in concurrent hash map. The value of a hash map is a Set collection.
 * 
 * @author Narendra Thite
 *
 */
@Service
public class TradeStore {

	private Map<String, Set<Trade>> tradeStoreMap = new ConcurrentHashMap<String, Set<Trade>>();
	
	/**
	 * Adds a trade object into hash map.
	 * 
	 * @param trade - trade object
	 */
	public void addToStore(Trade trade) {
		
		if (trade == null) {
			return;
		}
		
		if (!tradeStoreMap.containsKey(trade.getTradeId())) {
			tradeStoreMap.put(trade.getTradeId(), new TreeSet<Trade>());
		}
		
		tradeStoreMap.get(trade.getTradeId()).add(trade);

	}
	
	/**
	 * This method returns all the trades associated with a trade id
	 * 
	 * @param trade - Trade Object
	 * @return - a Set of trade objects associated with given trade.
	 */
	public Set<Trade> getTrades(Trade trade) {
		
		if (trade == null || trade.getTradeId() == null) {
			return null;
		}
		
		return tradeStoreMap.get(trade.getTradeId());
	}

	/**
	 * This method returns all the trades associated with a trade id
	 * 
	 * @param trade - Trade Object
	 * @return - a Set of trade objects associated with given trade.
	 */
	public Set<Trade> getTradesById(String tradeId) {
		
		if (StringUtils.hasText(tradeId)) {
			
			return tradeStoreMap.get(tradeId);
		}
		
		return null;
	}

	
	/**
	 * This method returns all the trades stored in Trade-Store Map
	 * 
	 * @return - set of trades stored in Trade-Store Map
	 */
	public Set<Trade> getAllTrades() {
		Set<Trade> set = new HashSet<Trade>();
		
		for (String key : tradeStoreMap.keySet()) {
			
			set.addAll(tradeStoreMap.get(key));
		}
		
		return set;
	}
}
