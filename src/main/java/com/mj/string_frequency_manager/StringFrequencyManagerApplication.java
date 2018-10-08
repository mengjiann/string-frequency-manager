package com.mj.string_frequency_manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class StringFrequencyManagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(StringFrequencyManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


	}
}
