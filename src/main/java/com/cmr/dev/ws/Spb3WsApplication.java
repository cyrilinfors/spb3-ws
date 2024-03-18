package com.cmr.dev.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.cmr.dev.ws.config.Spb3WsConfig;

@SpringBootApplication
@Import(Spb3WsConfig.class)
public class Spb3WsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spb3WsApplication.class, args);
	}

}
