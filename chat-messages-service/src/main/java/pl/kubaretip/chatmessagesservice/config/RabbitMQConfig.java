package pl.kubaretip.chatmessagesservice.config;

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
        return new FanoutExchange("pl.kubaretip.chatmessagesservice.fanout");
    }

    @Bean
    public Queue messageStoringQueue() {
        return new Queue("pl.kubaretip.chatmessagesservice.storing");
    }

    @Bean
    public Binding bindingActivationMail(FanoutExchange fanoutExchange, Queue usersActivationQueue) {
        return BindingBuilder.bind(usersActivationQueue).to(fanoutExchange);
    }


}
