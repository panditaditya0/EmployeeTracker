package com.ppus.psl_user_heartbeat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PslUserHeartbeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(PslUserHeartbeatApplication.class, args);
	}

}
