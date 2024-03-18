package com.cmr.dev.ws.config;

 import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.cmr.dev.business.config.Spb3BusinessConfig;

 
@Configuration
@OpenAPIDefinition(info = @Info(title = "spb3", version = "1.0", description = "Spring boot 3", contact = @Contact(name = "spb3")), security = {@SecurityRequirement(name = "bearerToken")})
@Import(Spb3BusinessConfig.class)
@ComponentScan(basePackages = {"com.cmr.dev.ws.*"})
public class Spb3WsConfig {

}
