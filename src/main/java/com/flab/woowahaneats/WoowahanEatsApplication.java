package com.flab.woowahaneats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WoowahanEatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WoowahanEatsApplication.class, args);
	}

}
