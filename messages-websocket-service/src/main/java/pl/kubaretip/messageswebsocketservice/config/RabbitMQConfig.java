package pl.kubaretip.messageswebsocketservice.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kubaretip.messageswebsocketservice.messaging.sender.StoringMessagesSender;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("pl.kubaretip.chatmessagesservice.fanout");
    }

    @Bean
    public StoringMessagesSender storingMessagesSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        return new StoringMessagesSender(rabbitTemplate, fanoutExchange);
    }


}
