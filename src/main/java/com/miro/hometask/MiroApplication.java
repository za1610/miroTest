package com.miro.hometask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableAutoConfiguration
@ComponentScan("com.miro.hometask")
public class MiroApplication {


	public static void main(String[] args) {
		SpringApplication.run(MiroApplication.class, args);
	}

}
