package ru.noleg.scootrent.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.exception.UserNotFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(ServiceException ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
        return this.buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return this.buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessLogicException(BusinessLogicException ex) {
        return this.buildResponse(HttpStatus.I_AM_A_TEAPOT, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleSqlException(DataIntegrityViolationException ex) {
        return this.buildResponse(
                HttpStatus.BAD_REQUEST,
                List.of("Database integrity violation.", "Probably an attempt to delete an entity associated with another entity."),
                ex
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleMissingRequestParamException(MissingServletRequestParameterException ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        return this.buildResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return this.buildResponse(HttpStatus.BAD_REQUEST, errors, ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
        return this.buildResponse(HttpStatus.FORBIDDEN, List.of(ex.getMessage()), ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleCommonException(Exception ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()), ex);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(HttpStatus status, List<String> messages, Throwable ex) {
        if (ex != null) {
            logger.error("Exception occurred: {}.", ex.getMessage(), ex);
        }
        return ResponseEntity.status(status)
                .body(new ExceptionResponse(
                        LocalDateTime.now(),
                        messages,
                        ex != null ? ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage() : null)
                );
    }
}
