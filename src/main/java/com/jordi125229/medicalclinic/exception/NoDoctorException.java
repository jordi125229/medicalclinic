package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class NoDoctorException extends MedicalClinicException {
    public NoDoctorException(String message, HttpStatus status) {
        super(message, status);
    }
}
