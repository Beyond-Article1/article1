package article1be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {

    // RestTemplate bean 주입
    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
