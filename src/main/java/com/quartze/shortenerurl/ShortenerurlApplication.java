package com.quartze.shortenerurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ShortenerurlApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortenerurlApplication.class, args);
		System.out.println("# Api Started");
		System.out.println("# OpenAPI Swagger documentation available at /swagger-ui/index.html" );
	}

}
