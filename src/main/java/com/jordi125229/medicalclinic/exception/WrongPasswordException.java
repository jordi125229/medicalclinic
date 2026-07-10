package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class WrongPasswordException extends MedicalClinicException {
    public WrongPasswordException(String message, HttpStatus status) {
        super(message, status);
    }
}
