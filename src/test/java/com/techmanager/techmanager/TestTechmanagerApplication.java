package com.techmanager.techmanager;

import org.springframework.boot.SpringApplication;

public class TestTechmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.from(TechmanagerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
