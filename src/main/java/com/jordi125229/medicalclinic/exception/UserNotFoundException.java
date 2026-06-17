package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends MedicalClinicException {
    public UserNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
