package com.cryptocurrency.demo;

import com.cryptocurrency.demo.controllers.ExtController;
import com.cryptocurrency.demo.entities.CryptoCurrency;
import com.cryptocurrency.demo.repositories.CurrencyRepository;
import com.cryptocurrency.demo.services.CurrencyService;
import com.cryptocurrency.demo.services.CurrencyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	@Autowired
	ExtController extController;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void doAfterStartup() throws IOException {
		extController.fetchAndSave(24, 100);
	}

}
