package pl.kubaretip.chatservice.messaging.sender;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

public class DeleteMessagesSender {

    private final RabbitTemplate template;
    private final FanoutExchange fanout;

    public DeleteMessagesSender(RabbitTemplate template, FanoutExchange fanout) {
        this.template = template;
        this.fanout = fanout;
    }

    public void sendDeletingMessagesTask(List<Long> friendChatIds) {
        var converter = template.getMessageConverter();
        var messageProperties = new MessageProperties();
        var message = converter.toMessage(friendChatIds, messageProperties);
        template.send(fanout.getName(), "", message);
    }

}
