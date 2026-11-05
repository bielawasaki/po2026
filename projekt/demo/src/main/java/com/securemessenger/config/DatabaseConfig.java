package com.securemessenger.config;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.mvstore.MVStoreModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class DatabaseConfig {

    @Bean(destroyMethod = "close")
    public Nitrite nitriteDatabase() throws Exception {

        Files.createDirectories(Path.of("data"));

        MVStoreModule storeModule = MVStoreModule
                .withConfig()
                .filePath("data/server.db")
                .build();

        return Nitrite.builder()
                .loadModule(storeModule)
                .openOrCreate();
    }
}