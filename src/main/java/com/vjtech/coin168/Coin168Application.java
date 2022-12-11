package com.vjtech.coin168;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Coin168Application {

	public static void main(String[] args) {
		SpringApplication.run(Coin168Application.class, args);
	}

}
