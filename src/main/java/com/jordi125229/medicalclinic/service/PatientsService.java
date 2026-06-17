package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.exception.WrongPasswordException;
import com.jordi125229.medicalclinic.model.CommandPatient;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.CommandPatientToUpdate;
import com.jordi125229.medicalclinic.model.mapper.PatientMapper;
import com.jordi125229.medicalclinic.model.entity.ChangePassword;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    public PatientDto createPatient(CommandPatient commandPatient) {
        User user = new User();
        user.setEmail(commandPatient.getEmail());
        validateEmail(user.getEmail());
        user.setPassword(commandPatient.getPassword());
        userRepository.save(user);
        Patient patient = new Patient(commandPatient.getId(), commandPatient.getIdCardNo(), commandPatient.getFirstName(),
                commandPatient.getLastName(), commandPatient.getPhoneNumber(), commandPatient.getBirthday(), user);
        Patient patientBeforeMapping = patientsRepository.save(patient);
        return patientMapper.patientToDto(patientBeforeMapping);
    }

    public PatientDto getPatientDto(String email) {
        Patient patient = getPatientByEmail(email);
        return patientMapper.patientToDto(patient);
    }

    public void deletePatient(String email) {
        Patient patient = getPatientByEmail(email);
        patientsRepository.delete(patient);
    }

    public void editPatient(String email, CommandPatientToUpdate patient) {
        Patient patientByEmail = getPatientByEmail(email);
        validateEmailForUpdatePatient(email, patientByEmail.getId());
        patientByEmail.editPatient(patient);
        patientsRepository.save(patientByEmail);
    }

//    public void changePassword(String email, ChangePassword changePassword) {
//        Patient patient = getPatientByEmail(email);
//        if (changePassword.getPassword().equals(patient.getPassword())) {
//            patient.setPassword(changePassword.getNewPassword());
//        } else {
//            throw new WrongPasswordException("Wrong password!", HttpStatus.BAD_REQUEST);
//        }
//        patientsRepository.save(patient);
//    }

    public Patient getPatientByEmail(String email) {
        return patientsRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
    }

    private void validateEmail(String email) {
        Optional<Patient> patientFoundByEmail = patientsRepository.findByUserEmail(email);
        if (patientFoundByEmail.isPresent()) {
            throw new PatientsEmailAlreadyExists("Patient's email already exists!", HttpStatus.CONFLICT);
        }
    }

    private void validateEmailForUpdatePatient(String email, Long id) {
        Optional<Patient> patientFound = patientsRepository.findByUserEmailAndIdNot(email, id);
        if (patientFound.isPresent()) {
            throw new PatientsEmailAlreadyExists("Patient's email already exists!", HttpStatus.CONFLICT);
        }
    }
}


