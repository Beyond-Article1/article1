package article1be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Article1BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Article1BeApplication.class, args);
    }

}
