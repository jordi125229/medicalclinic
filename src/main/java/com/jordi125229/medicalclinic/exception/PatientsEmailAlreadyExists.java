package com.jordi125229.medicalclinic.exception;

import org.springframework.http.HttpStatus;

public class PatientsEmailAlreadyExists extends MedicalClinicException {
    public PatientsEmailAlreadyExists(String message, HttpStatus status) {
        super(message, status);
    }
}
