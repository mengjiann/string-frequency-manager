package com.mj.string_frequency_manager;

import com.mj.string_frequency_manager.config.constant.AppConstant;
import com.mj.string_frequency_manager.string_record.exception.InvalidStringRecordLogFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.embedded.Redis;
import redis.embedded.RedisServer;

import javax.annotation.PreDestroy;
import java.io.File;
import java.time.LocalDateTime;

@Slf4j
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
		log.info("Stopping redis server.");
		redisServer.stop();
	}
}
