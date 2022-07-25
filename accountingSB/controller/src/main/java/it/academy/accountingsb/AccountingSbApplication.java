package it.academy.accountingsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@Configuration
@SpringBootApplication(scanBasePackages = {"it.academy.accountingsb"})
public class AccountingSbApplication {


    public static void main(String[] args) {
        SpringApplication.run(AccountingSbApplication.class, args);
    }

    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }


}
