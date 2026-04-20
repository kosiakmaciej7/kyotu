package pl.kyotu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("pl.kyotu.config")
@EnableScheduling
public class KyotuApplication {
    public static void main(String[] args) {
        SpringApplication.run(KyotuApplication.class, args);
    }
}
