package com.bat.aclgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AclGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclGatewayApplication.class);
    }

}
