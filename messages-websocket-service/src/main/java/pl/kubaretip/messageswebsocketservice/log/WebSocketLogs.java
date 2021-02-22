package pl.kubaretip.messageswebsocketservice.log;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.WebSocketMessageBrokerStats;

import javax.annotation.PostConstruct;

@Profile("dev")
@Component
public class WebSocketLogs {

    private final WebSocketMessageBrokerStats webSocketMessageBrokerStats;

    public WebSocketLogs(WebSocketMessageBrokerStats webSocketMessageBrokerStats) {
        this.webSocketMessageBrokerStats = webSocketMessageBrokerStats;
    }

    @PostConstruct
    public void init() {
        webSocketMessageBrokerStats.setLoggingPeriod(30 * 1000);
    }


}
