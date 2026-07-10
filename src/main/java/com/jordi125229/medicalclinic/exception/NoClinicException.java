package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class NoClinicException extends MedicalClinicException{
    public NoClinicException(String message, HttpStatus status) {
        super(message, status);
    }
}
