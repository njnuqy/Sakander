package com.sakander.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class DatabaseConfig {
    private String url;
    private String username;
    private String password;

    public DatabaseConfig() throws IOException {
        Properties properties = new Properties();
        try(InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")){
            if(input == null){
                System.out.println("Sorry, unable to find database.properties");
                return;
            }
            properties.load(input);
            this.url = properties.getProperty("sakander.url");
            this.username = properties.getProperty("sakander.username");
            this.password = properties.getProperty("sakander.password");
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


}
