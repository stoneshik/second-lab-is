package lab.is.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lab.is.dto.responses.ErrorMessageResponseDto;
import lab.is.exceptions.IncorrectDtoInRequestException;
import lab.is.exceptions.NestedObjectIsUsedException;
import lab.is.exceptions.NestedObjectNotFoundException;
import lab.is.exceptions.NotFoundException;
import lab.is.exceptions.ResourceIsAlreadyExistsException;
import lab.is.exceptions.TokenRefreshException;
import lab.is.exceptions.ValueOverflowException;

@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageResponseDto handleException(NotFoundException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(NestedObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessageResponseDto handleException(NestedObjectNotFoundException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(IncorrectDtoInRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageResponseDto handleException(IncorrectDtoInRequestException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(ResourceIsAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageResponseDto handleException(ResourceIsAlreadyExistsException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(NestedObjectIsUsedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageResponseDto handleException(NestedObjectIsUsedException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(ValueOverflowException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageResponseDto handleException(ValueOverflowException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessageResponseDto handleException(OptimisticLockException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message("Объект изменен другим пользователем")
            .build();
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageResponseDto handleException(TokenRefreshException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageResponseDto handleException(AccessDeniedException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorMessageResponseDto handleException(AuthenticationException e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message(e.getMessage())
            .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageResponseDto handleException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(fieldError ->
            errors.put(
                fieldError.getField(),
                fieldError.getDefaultMessage()
            )
        );
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message("Переданы неправильные значения")
            .violations(errors)
            .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessageResponseDto handleException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Map<String, String> errors = new HashMap<>();
        violations.forEach(violation ->
            errors.put(
                violation.getPropertyPath().toString(),
                violation.getMessage()
            )
        );
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message("Переданы неправильные значения")
            .violations(errors)
            .build();
    }

    @ExceptionHandler(InternalServerError.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponseDto handleException(InternalServerError e) {
        return ErrorMessageResponseDto.builder()
            .timestamp(new Date())
            .message("Ошибка сервера")
            .build();
    }
}
