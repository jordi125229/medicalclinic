package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.model.ChangePassword;
import com.jordi125229.medicalclinic.model.Patient;
import com.jordi125229.medicalclinic.repository.PatientsRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientsService {
    private PatientsRepository patientsRepository;

    public PatientsService(PatientsRepository patientsRepository) {
        this.patientsRepository = patientsRepository;
    }

    public List<Patient> getPatients() {
        return patientsRepository.findPatients();
    }

    public Patient createPatient(Patient patient) {
        patientsRepository.addPatient(patient);
        return patient;
    }

    public Patient getPatientByEmail(String email) {
        return patientsRepository.returnPatientByEmail(email).get();
    }

    public void deletePatient(String email) {
        patientsRepository.deletePatientByEmail(email);
    }

    public Patient editPatient(String email, Patient patient) {
        Patient patientByEmail = getPatientByEmail(email);
        patientByEmail.setEmail(patient.getEmail());
        patientByEmail.setPassword(patient.getPassword());
        patientByEmail.setIdCardNo(patient.getIdCardNo());
        patientByEmail.setFirstName(patient.getFirstName());
        patientByEmail.setLastName(patient.getLastName());
        patientByEmail.setPhoneNumber(patient.getPhoneNumber());
        patientByEmail.setBirthday(patient.getBirthday());
        return patientByEmail;
    }

    public void changePassword(String email, ChangePassword changePassword) {
        Patient patient = getPatientByEmail(email);
        if (changePassword.getPassword().equals(patient.getPassword())) {
            patient.setPassword(changePassword.getNewPassword());
        }
    }
}

