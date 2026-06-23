package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class WrongTimeException extends MedicalClinicException{
    public WrongTimeException(String message, HttpStatus status) {
        super(message, status);
    }
}
