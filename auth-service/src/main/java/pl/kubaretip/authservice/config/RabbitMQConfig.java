package pl.kubaretip.authservice.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kubaretip.authservice.messaging.sender.UserSender;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("pl.kubaretip.authservice.fanout");
    }


    @Bean
    public UserSender userSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        return new UserSender(rabbitTemplate, fanoutExchange);
    }


}
