package com.cryptocurrency.demo.context;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan({"com.cryptocurrency.demo.controllers", "com.cryptocurrency.demo.services"})
@EnableMongoRepositories({"com.cryptocurrency.demo.repositories"})
@EntityScan({"com.cryptocurrency.demo.entities"})
public class Config {
}