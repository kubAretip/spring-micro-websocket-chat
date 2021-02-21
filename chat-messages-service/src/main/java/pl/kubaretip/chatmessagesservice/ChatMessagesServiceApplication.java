package pl.kubaretip.chatmessagesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ChatMessagesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMessagesServiceApplication.class, args);
    }

}
