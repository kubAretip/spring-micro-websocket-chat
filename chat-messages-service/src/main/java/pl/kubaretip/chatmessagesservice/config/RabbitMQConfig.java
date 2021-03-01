package pl.kubaretip.chatmessagesservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String MESSAGE_STORING_QUEUE = "pl.kubaretip.chatmessagesservice.storing";
    private static final String MESSAGE_STORING_EXCHANGE = "pl.kubaretip.chatmessagesservice.fanout";
    private static final String MESSAGE_STORING_DLQ = MESSAGE_STORING_QUEUE + ".dlq";
    private static final String MESSAGE_STORING_DLE = MESSAGE_STORING_QUEUE + ".dlx";
    private static final String MESSAGE_DELETING_QUEUE = "pl.kubaretip.chatmessagesservice.deleting";
    private static final String MESSAGE_DELETING_EXCHANGE = "pl.kubaretip.chatmessagesservice.fanout.deleting";

    @Bean
    public FanoutExchange messageStoringExchange() {
        return new FanoutExchange(MESSAGE_STORING_EXCHANGE);
    }

    @Bean
    public Queue messageStoringQueue() {
        return QueueBuilder.durable(MESSAGE_STORING_QUEUE)
                .deadLetterExchange(MESSAGE_STORING_DLE)
                .build();
    }

    @Bean
    public Binding messageStoringBinding(Queue messageStoringQueue, FanoutExchange messageStoringExchange) {
        return BindingBuilder.bind(messageStoringQueue).to(messageStoringExchange);
    }

    @Bean
    public FanoutExchange messageDeletingExchange() {
        return new FanoutExchange(MESSAGE_DELETING_EXCHANGE);
    }

    @Bean
    public Queue messageDeletingQueue() {
        return QueueBuilder.durable(MESSAGE_DELETING_QUEUE).build();
    }

    @Bean
    public Binding messageDeletingBinding(Queue messageDeletingQueue, FanoutExchange messageDeletingExchange) {
        return BindingBuilder.bind(messageDeletingQueue).to(messageDeletingExchange);
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(MESSAGE_STORING_DLE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(MESSAGE_STORING_DLQ)
                .ttl(5000)
                .build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }

}
