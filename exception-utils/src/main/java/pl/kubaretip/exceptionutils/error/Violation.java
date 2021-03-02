package pl.kubaretip.exceptionutils.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Violation {
    private String field;
    private List<String> message;
}
