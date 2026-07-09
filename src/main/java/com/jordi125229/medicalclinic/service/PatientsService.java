package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.model.mapper.PatientMapper;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientsService {
    private final PatientRepository patientsRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public PageableDto<PatientDto> getPatients(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Patient> patientsPage = patientsRepository.findAll(pageable);
        List<PatientDto> patients = patientsPage.stream()
                .map(patientMapper::patientToDto)
                .toList();
        return PageableDto.create(patients, patientsPage);
    }

    @Transactional
    public PatientDto createPatient(CreatePatientCommand createPatientCommend) {
        User user = createUser(createPatientCommend);
        userRepository.save(user);
        Patient patient = new Patient(null, createPatientCommend.getIdCardNo(), createPatientCommend.getFirstName(),
                createPatientCommend.getLastName(), createPatientCommend.getPhoneNumber(), createPatientCommend.getBirthday(), user, null);
        Patient patientEntity = patientsRepository.save(patient);
        return patientMapper.patientToDto(patientEntity);
    }

    public PatientDto getPatientDto(String email) {
        Patient patient = getPatientByEmail(email);
        return patientMapper.patientToDto(patient);
    }

    @Transactional
    public void deletePatient(String email) {
        Patient patient = patientsRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
        patient.getUser().setPatient(null);
        patientsRepository.delete(patient);
    }

    @Transactional
    public PatientDto editPatient(String email, UpdatePatientCommand patient) {
        Patient patientByEmail = getPatientByEmail(email);
        validateEmailForUpdatePatient(email, patientByEmail.getId());
        patientByEmail.editPatient(patient);
        patientsRepository.save(patientByEmail);
        return patientMapper.patientToDto(patientByEmail);
    }

    private Patient getPatientByEmail(String email) {
        return patientsRepository.findByUserEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient wasn't found!", HttpStatus.NOT_FOUND));
    }

    private User createUser(CreatePatientCommand createPatientCommend) {
        validateEmail(createPatientCommend.getEmail());
        User user = new User();
        user.setEmail(createPatientCommend.getEmail());
        user.setPassword(createPatientCommend.getPassword());
        return user;
    }

    private void validateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new PatientsEmailAlreadyExists("User with this email already exists!", HttpStatus.CONFLICT);
        }
    }

    private void validateEmailForUpdatePatient(String email, Long id) {
        Optional<Patient> patientFound = patientsRepository.findByUserEmailAndIdNot(email, id);
        if (patientFound.isPresent()) {
            throw new PatientsEmailAlreadyExists("Patient with this email already exists!", HttpStatus.CONFLICT);
        }
    }
}


