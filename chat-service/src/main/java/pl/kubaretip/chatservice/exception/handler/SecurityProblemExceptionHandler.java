package pl.kubaretip.chatservice.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class SecurityProblemExceptionHandler implements SecurityAdviceTrait {
}
