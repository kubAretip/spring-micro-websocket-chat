package pl.kubaretip.chatservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("pl.kubaretip.authservice.fanout");
    }

    @Bean
    public Queue newUsersQueue() {
        return new Queue("pl.kubaretip.chatservice.account");
    }

    @Bean
    public Binding bindingActivationMail(FanoutExchange fanoutExchange, Queue newUsersQueue) {
        return BindingBuilder.bind(newUsersQueue).to(fanoutExchange);
    }


}
