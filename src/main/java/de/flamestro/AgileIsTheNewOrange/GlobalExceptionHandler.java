package de.flamestro.AgileIsTheNewOrange;

import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import de.flamestro.AgileIsTheNewOrange.web.model.ApiError;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<ApiError> handleInvalidNameException() {
        return ResponseEntity.badRequest().body(ApiError.builder().status(Status.INVALID_NAME).build());
    }
}
