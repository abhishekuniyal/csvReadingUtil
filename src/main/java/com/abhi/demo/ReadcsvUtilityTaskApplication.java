package com.abhi.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@SpringBootApplication
@EnableTask
public class ReadcsvUtilityTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadcsvUtilityTaskApplication.class, args);
	}

}
