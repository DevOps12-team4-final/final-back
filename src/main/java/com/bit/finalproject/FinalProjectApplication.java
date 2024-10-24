package com.bit.finalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
<<<<<<< HEAD
=======
@EnableJpaAuditing

>>>>>>> 35a1433166b1f1cd73cb567dd787c98e3a848c70
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }

}
