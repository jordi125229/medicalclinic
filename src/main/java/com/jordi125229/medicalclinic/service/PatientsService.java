package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.model.CommandPatient;
import com.jordi125229.medicalclinic.model.mapper.PatientMapper;
import com.jordi125229.medicalclinic.model.entity.ChangePassword;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.repository.PatientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PatientsService {
    private final PatientsRepository patientsRepository;
    private final PatientMapper patientMapper;

    public List<PatientDto> getPatients() {
        return patientsRepository.returnPatients().stream()
                .map(patientMapper::patientToDto)
                .toList();
    }

    public PatientDto createPatient(CommandPatient commandPatient) {
        Patient patient = new Patient(commandPatient.getEmail(), commandPatient.getPassword(), commandPatient.getIdCardNo(), commandPatient.getFirstName(),
                commandPatient.getLastName(), commandPatient.getPhoneNumber(), commandPatient.getBirthday());
        validateEmail(patient);
        Patient patientBeforeMapping = patientsRepository.addPatient(patient);
        return patientMapper.patientToDto(patientBeforeMapping);
    }

    public Patient getPatientByEmail(String email) {
        return patientsRepository.returnPatientByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
    }

    public PatientDto getPatientDto(String email) {
        Patient patient = getPatientByEmail(email);
        return patientMapper.patientToDto(patient);
    }

    public void deletePatient(String email) {
        patientsRepository.deletePatient(email);
    }

    public void editPatient(String email, Patient patient) {
        validateEmail(patient);
        Patient patientByEmail = getPatientByEmail(email);
        patientByEmail.editPatient(patient);
    }

    public void changePassword(String email, ChangePassword changePassword) {
        Patient patient = getPatientByEmail(email);
        if (changePassword.getPassword().equals(patient.getPassword())) {
            patient.setPassword(changePassword.getNewPassword());
        }
    }

    private void validateEmail(Patient patient) {
        Optional<Patient> patientFoundByEmail = patientsRepository.returnPatientByEmail(patient.getEmail());
        if (patientFoundByEmail.isPresent()) {
            throw new PatientsEmailAlreadyExists("Patient's email already exists!", HttpStatus.CONFLICT);
        }
    }
}


