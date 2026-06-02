package com.jordi125229.medicalclinic.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MedicalClinicExceptionHandler {

    @ExceptionHandler(MedicalClinicException.class)
    public ResponseEntity<ErrorMessage> handleException(MedicalClinicException exception){
        return ResponseEntity.status(exception.getStatus()).body(new ErrorMessage(exception.getMessage(), exception.getStatus()));
    }
}
