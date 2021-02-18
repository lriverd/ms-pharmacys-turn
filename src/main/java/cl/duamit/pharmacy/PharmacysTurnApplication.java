package cl.duamit.pharmacy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
public class PharmacysTurnApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(PharmacysTurnApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
