package ru.elsu.oio.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ErrorDetail {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ErrorDetail(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ErrorDetail(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}