package pl.kubaretip.mailservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    static final String queueName = "pl.kubaretip.mailservice.users.activation";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

}
