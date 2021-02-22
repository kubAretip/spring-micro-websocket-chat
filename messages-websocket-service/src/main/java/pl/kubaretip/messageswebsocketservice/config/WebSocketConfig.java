package pl.kubaretip.messageswebsocketservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import pl.kubaretip.messageswebsocketservice.security.AuthChannelInterceptor;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptor authChannelInterceptor;
    private final String clientLogin;
    private final String clientPasscode;
    private final String systemLogin;
    private final String systemPasscode;
    private final int relayPort;
    private final String relayHost;

    public WebSocketConfig(AuthChannelInterceptor authChannelInterceptor,
                           @Value("${websocket.rabbitmq.stomp.clientLogin:guest}") String clientLogin,
                           @Value("${websocket.rabbitmq.stomp.clientPasscode:guest}") String clientPasscode,
                           @Value("${websocket.rabbitmq.stomp.systemLogin:guest}") String systemLogin,
                           @Value("${websocket.rabbitmq.stomp.systemPasscode:guest}") String systemPasscode,
                           @Value("${websocket.rabbitmq.stomp.relayPort:61613}") int relayPort,
                           @Value("${websocket.rabbitmq.stomp.relayHost:127.0.0.1}") String relayHost) {
        this.authChannelInterceptor = authChannelInterceptor;
        this.clientLogin = clientLogin;
        this.clientPasscode = clientPasscode;
        this.systemLogin = systemLogin;
        this.systemPasscode = systemPasscode;
        this.relayPort = relayPort;
        this.relayHost = relayHost;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .setApplicationDestinationPrefixes("/app")
                .enableStompBrokerRelay("/topic/")
                .setUserDestinationBroadcast("/topic/log-unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry")
                .setSystemLogin(this.systemLogin)
                .setSystemPasscode(this.systemPasscode)
                .setClientLogin(this.clientLogin)
                .setClientPasscode(this.clientPasscode)
                .setRelayHost(this.relayHost)
                .setRelayPort(this.relayPort);
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        var resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        var converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);

        messageConverters.add(converter);

        return false;
    }


}
