package com.caseStudy.E_commerce.ExceptionHandler;


import com.caseStudy.E_commerce.DTO.Error.ErrorResponseDTO;
import com.caseStudy.E_commerce.DTO.Error.ValidationErrorResponseDTO;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation Failed
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> validationError(
        MethodArgumentNotValidException ex, HttpServletRequest request
    ){
        HashMap<String,String> fieldError = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error)->{
            fieldError.put(error.getField(),error.getDefaultMessage());
        });
        ValidationErrorResponseDTO errorResponseDTO = new ValidationErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Field",
                request.getRequestURI(),
                fieldError
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponseDTO);
    }

    // HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    // malformed wrong DataType Json
    public ResponseEntity<ErrorResponseDTO> handleNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request
    ){
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body. Please check your JSON format and data types.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    // path Parameter Problem
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex , HttpServletRequest request){
        String parameterName = ex.getName();
        Object invalidValue = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();

        String expectedType = requiredType != null
                ? requiredType.getSimpleName()
                : "unknown";

        String message = String.format(
                "Invalid value '%s' for parameter '%s'. Expected type: %s.",
                invalidValue,
                parameterName,
                expectedType
        );

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
    // Resource Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex, HttpServletRequest request){

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
    }





}
