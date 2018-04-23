package io.lsn.dailydocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
public class DailydockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailydockerApplication.class, args);
    }

}
