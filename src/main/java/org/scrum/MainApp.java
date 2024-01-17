package org.scrum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;



@SpringBootApplication(scanBasePackages = "org.scrum")
public class MainApp extends SpringBootServletInitializer  {



    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);
    }


}
