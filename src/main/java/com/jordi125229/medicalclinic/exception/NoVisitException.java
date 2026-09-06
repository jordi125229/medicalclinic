package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class NoVisitException extends MedicalClinicException{
    public NoVisitException(String message, HttpStatus status) {
        super(message, status);
    }
}
