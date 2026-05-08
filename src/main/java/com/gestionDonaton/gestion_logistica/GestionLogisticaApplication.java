package com.gestionDonaton.gestion_logistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class GestionLogisticaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionLogisticaApplication.class, args);
	}

}
