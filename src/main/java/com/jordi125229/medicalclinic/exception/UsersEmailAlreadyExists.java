package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class UsersEmailAlreadyExists extends MedicalClinicException {
    public UsersEmailAlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
