package com.num.digital_ticket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.num.digital_ticket.mapper")
public class DigitalTicketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalTicketApplication.class, args);
	}

}
