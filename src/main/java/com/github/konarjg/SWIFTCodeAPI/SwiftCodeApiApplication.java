package com.github.konarjg.SWIFTCodeAPI;

import com.github.konarjg.SWIFTCodeAPI.auth.AppJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties(AppJwtProperties.class)
public class SwiftCodeApiApplication{
	public static void main(String[] args) {
		SpringApplication.run(SwiftCodeApiApplication.class, args);
	}
}
