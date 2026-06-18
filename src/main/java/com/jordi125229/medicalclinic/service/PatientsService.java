package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.model.mapper.PatientMapper;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PatientsService {
    private final PatientRepository patientsRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public List<PatientDto> getPatients() {
        return patientsRepository.findAll().stream()
                .map(patientMapper::patientToDto)
                .toList();
    }

    public PatientDto createPatient(CreatePatientCommand createPatientCommend) {
        User user = createUser(createPatientCommend);
        userRepository.save(user);
        Patient patient = new Patient(null, createPatientCommend.getIdCardNo(), createPatientCommend.getFirstName(),
                createPatientCommend.getLastName(), createPatientCommend.getPhoneNumber(), createPatientCommend.getBirthday(), user);
        Patient patientEntity = patientsRepository.save(patient);
        return patientMapper.patientToDto(patientEntity);
    }

    private User createUser(CreatePatientCommand createPatientCommend) {
        validateEmail(createPatientCommend.getEmail());
        User user = new User();
        user.setEmail(createPatientCommend.getEmail());
        user.setPassword(createPatientCommend.getPassword());
        return user;
    }

    public PatientDto getPatientDto(String email) {
        Patient patient = getPatientByEmail(email);
        return patientMapper.patientToDto(patient);
    }

    public void deletePatient(String email) {
        Patient patient = getPatientByEmail(email);
        patientsRepository.delete(patient);
    }

    public void editPatient(String email, UpdatePatientCommand patient) {
        Patient patientByEmail = getPatientByEmail(email);
        validateEmailForUpdatePatient(email, patientByEmail.getId());
        patientByEmail.editPatient(patient);
        patientsRepository.save(patientByEmail);
    }

    public Patient getPatientByEmail(String email) {
        return patientsRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
    }

    private void validateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new PatientsEmailAlreadyExists("User's email already exists!", HttpStatus.CONFLICT);
        }
    }

    private void validateEmailForUpdatePatient(String email, Long id) {
        Optional<Patient> patientFound = patientsRepository.findByUserEmailAndIdNot(email, id);
        if (patientFound.isPresent()) {
            throw new PatientsEmailAlreadyExists("Patient's email already exists!", HttpStatus.CONFLICT);
        }
    }
}


