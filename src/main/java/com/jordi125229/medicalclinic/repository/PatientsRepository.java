package com.jordi125229.medicalclinic.repository;

import com.jordi125229.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientsRepository {
    private final List<Patient> patients;

    public PatientsRepository() {
        patients = new ArrayList<>();
    }

    public List<Patient> findPatients() {
        return patients;
    }

    public Optional<Patient> returnPatientByEmail(String email) {
        return patients.stream().filter(patient -> patient.getEmail().equals(email)).findFirst();
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void deletePatientByEmail(String email) {
        returnPatientByEmail(email).ifPresent(patients::remove);
    }
}
