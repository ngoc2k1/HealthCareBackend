package vn.healthcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.healthcare.dto.BaseResponse;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public BaseResponse handleBadRequest(BadRequestException e) {
        return BaseResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .msg(e.getMessage())
                .data(e.getData())
                .build();
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse handleAuthenticationException(AccessDeniedException ex) {
        return BaseResponse.builder()
                .code(403)
                .msg("Yêu cầu đăng nhập").build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, String> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public BaseResponse handleNotFound(NotFoundException e) {
        return BaseResponse.builder()
                .code(404)
                .msg(e.getMessage())
                .build();
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public BaseResponse handleConflict(ConflictException e) {
        return BaseResponse.builder()
                .code(409)
                .msg(e.getMessage())
                .data(e.getData())
                .build();
    }
}
