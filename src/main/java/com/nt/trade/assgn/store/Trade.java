package com.nt.trade.assgn.store;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

/**
 * Trade domain object containing all trade attributes. It implements Comparable interface to store the trades in ascending order of it's version.
 * 
 * @author Narendra Thite
 *
 */
public class Trade implements Comparable<Trade> {

	private String tradeId;
	private Integer version;
	private String counterPartyId;
	private String bookId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	private Date maturityDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	private Date createdDate;
	
	private String expired = "N";

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCounterPartyId() {
		return counterPartyId;
	}

	public void setCounterPartyId(String counterPartyId) {
		this.counterPartyId = counterPartyId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	@Override
	public int compareTo(Trade o) {
		return (o != null && this.version != null && o.version != null) ? version.compareTo(o.getVersion()) : 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Trade)) {
			return false;
		}
		
		Trade t1 = (Trade) obj;
		
		if (this.tradeId.equals(t1.getTradeId()) && 
				this.version.equals(t1.getVersion()) &&
				this.bookId.equals(t1.getBookId()) && 
				this.counterPartyId.equals(t1.getCounterPartyId()) &&
				this.maturityDate.equals(t1.getMaturityDate()) && 
				this.expired.equals(t1.getExpired())
				) {
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		
		int hash = 7;
	
		hash = 19 * this.tradeId.hashCode();
		hash = 19 * this.version.hashCode();
		hash = 19 * this.bookId.hashCode();
		hash = 19 * this.counterPartyId.hashCode();
		hash = 19 * this.maturityDate.hashCode();
		hash = 19 * this.expired.hashCode();
		
		return hash;
		
	}
	
	@Override
	public String toString() {
		return "tradeId: " + tradeId + " version: " + version + " bookId: " + bookId + " counterPartyId: " + counterPartyId + 
				" maturityDate: " + maturityDate + " createdDate: " + createdDate + " expired: " + expired + "\n";
	}
}
