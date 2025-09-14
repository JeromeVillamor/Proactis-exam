package com.proactis.pma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ValidationError {
    private String message = "Oops. Something went wrong!";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public static ValidationError errors(Map<String, String> errors) {
        ValidationError validationError = new ValidationError();
        validationError.setErrors(errors);

        return validationError;
    }

    public static ValidationError message(String message) {
        ValidationError validationError = new ValidationError();
        validationError.setMessage(message);

        return validationError;
    }
}
