package pl.kubaretip.authservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
public class Error {

    private int status;
    private String title;
    private String detail;

}
