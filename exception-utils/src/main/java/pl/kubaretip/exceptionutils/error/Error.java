package pl.kubaretip.exceptionutils.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class Error {

    @Builder.Default
    private final Date timestamp = new Date();
    private int status;
    private String title;
    private String detail;
    private String path;
    private Set<Violation> violations;
}
