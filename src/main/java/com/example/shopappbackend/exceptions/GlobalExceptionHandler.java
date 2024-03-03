package com.example.shopappbackend.exceptions;

import com.example.shopappbackend.responses.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> handleGeneralException(Exception ex){
        return ResponseEntity.internalServerError().body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build()
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> handleDataNotFoundException(DataNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ResponseObject.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .build()
        );
    }

//    @ExceptionHandler(ExpiredTokenException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<ResponseObject> handleExpiredTokenException(ExpiredTokenException ex){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                ResponseObject.builder()
//                        .timeStamp(LocalDateTime.now())
//                        .message(ex.getMessage())
//                        .status(HttpStatus.BAD_REQUEST)
//                        .statusCode(HttpStatus.BAD_REQUEST.value())
//                        .build()
//        );
//    }


}
