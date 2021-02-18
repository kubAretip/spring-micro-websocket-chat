package pl.kubaretip.authservice.exception.controllerAdvice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class ProblemExceptionHandler implements ProblemHandling {
}
