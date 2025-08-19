package si.afridau.commerce.checkout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@EnableMethodSecurity
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = {"si.afridau.commerce.checkout", "si.afridau.commerce.auth", "si.afridau.commerce.exception"})
public class CheckoutApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckoutApplication.class, args);
    }

}
