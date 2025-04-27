package ru.noleg.scootrent.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity<ExceptionResponse> handleServiceException(ServiceException ex) {
//        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()), ex.getCause());
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException ex) {
//        return this.buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), ex.getCause());
//    }
//
//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
//        return this.buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()), ex.getCause());
//    }
//
//    @ExceptionHandler(BusinessLogicException.class)
//    public ResponseEntity<ExceptionResponse> handleBusinessLogicException(BusinessLogicException ex) {
//        return this.buildResponse(HttpStatus.I_AM_A_TEAPOT, List.of(ex.getMessage()), ex.getCause());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                .toList();
//
//        return this.buildResponse(HttpStatus.BAD_REQUEST, errors, ex);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
//        return this.buildResponse(HttpStatus.FORBIDDEN, List.of(ex.getMessage()), ex);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ExceptionResponse> handleCommonException(Exception ex) {
//        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getMessage()), ex.getCause());
//    }
//
//    private ResponseEntity<ExceptionResponse> buildResponse(HttpStatus status, List<String> messages, Throwable cause) {
//        return ResponseEntity.status(status)
//                .body(new ExceptionResponse(
//                        LocalDateTime.now(),
//                        messages,
//                        cause != null ? cause.getMessage() : null)
//                );
//    }
}
