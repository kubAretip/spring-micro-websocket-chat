package pl.kubaretip.mailservice.config;

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
    public Queue usersActivationQueue() {
        return new Queue("pl.kubaretip.mailservice.activation");
    }

    @Bean
    public Binding bindingActivationMail(FanoutExchange fanoutExchange, Queue usersActivationQueue) {
        return BindingBuilder.bind(usersActivationQueue).to(fanoutExchange);
    }


}
