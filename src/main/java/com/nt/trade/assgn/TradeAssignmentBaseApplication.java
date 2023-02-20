package com.nt.trade.assgn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring boot base class for Trade Assignment
 * 
 * @author Narendra Thite
 *
 */

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableAsync
@EnableScheduling
@ComponentScan("com.nt.trade.assgn")
public class TradeAssignmentBaseApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(TradeAssignmentBaseApplication.class, args);
		
	}
}
