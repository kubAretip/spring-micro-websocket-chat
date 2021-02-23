package pl.kubaretip.chatmessagesservice.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import pl.kubaretip.authutils.security.SecurityUserDetails;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<String> getCurrentUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getPrincipal())
                .cast(SecurityUserDetails.class)
                .map(SecurityUserDetails::getId);
    }


}
