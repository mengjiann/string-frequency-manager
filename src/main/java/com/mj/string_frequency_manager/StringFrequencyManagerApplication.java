package com.mj.string_frequency_manager;

import com.mj.string_frequency_manager.util.InputValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;

@Slf4j
@EnableWebMvc
@EnableScheduling
@SpringBootApplication
public class StringFrequencyManagerApplication implements CommandLineRunner {

	private RedisServer redisServer;

	private ApplicationInitializationUtil appLoadTestData;

	public StringFrequencyManagerApplication(RedisServer redisServer, ApplicationInitializationUtil appLoadTestData) {
		this.redisServer = redisServer;
		this.appLoadTestData = appLoadTestData;
	}

	public static void main(String[] args) {
		SpringApplication.run(StringFrequencyManagerApplication.class, args);
	}

	/*
		Process initial load for past 24 hours log files.
	 */
	@Override
	public void run(String... args) throws Exception {
			appLoadTestData.generateTestData();
			appLoadTestData.loadInitData();
	}

	@PreDestroy
	public void shutdown(){
		if(!InputValidator.isInvalid(redisServer) && redisServer.isActive()){
		    log.info("Stopping redis server.");
		    redisServer.stop();
        }
	}
}
