package pl.kubaretip.authservice.exception.controllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

@ControllerAdvice
public class SecurityProblemExceptionHandler implements SecurityAdviceTrait {
}
