package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.PatientMapper;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PatientsServiceTest {

    private PatientsService patientsService;
    private PatientMapper patientsMapper;
    private PatientRepository patientRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientsMapper = Mappers.getMapper(PatientMapper.class);
        this.patientsService = new PatientsService(patientRepository, patientsMapper, userRepository);
    }

    @Test
    void getPatients_DataCorrect_DataGotten() {
        // given
        int pageNumber = 0;
        int pageSize = 1;
        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .build();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Patient> patients = List.of(patient);
        Page<Patient> patientsPage = new PageImpl<>(patients, pageRequest, patients.size());

        when(patientRepository.findAll(pageRequest)).thenReturn(patientsPage);

        // when
        PageableDto<PatientDto> patientDtoPage = patientsService.getPatients(pageNumber, pageSize);

        // then
        assertAll(
                () -> assertEquals(patients.size(), patientDtoPage.getContent().size()),
                () -> assertEquals(pageSize, patientDtoPage.getTotalPages()),
                () -> assertEquals(pageNumber, patientDtoPage.getPageNumber())
        );
    }

    @Test
    void createPatient_DataCorrect_PatientCreated() {
        // given
        CreatePatientCommand command = CreatePatientCommand.builder()
                .email("email")
                .password("password")
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .build();

        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .user(user)
                .build();

        when(patientRepository.findByUserEmail(command.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(patientRepository.save(any())).thenReturn(patient);

        // when
        PatientDto result = patientsService.createPatient(command);

        // then
        assertAll(
                () -> assertEquals("email", result.getEmail()),
                () -> assertEquals("name", result.getFirstName()),
                () -> assertEquals("lastName", result.getLastName()),
                () -> assertEquals(LocalDate.of(2025, 12, 1), result.getBirthday())
        );
    }

    @Test
    void getPatientDto_DataCorrect_PatientDtoGotten() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .user(user)
                .build();
        when(patientRepository.findByUserEmail(patient.getUser().getEmail())).thenReturn(Optional.of(patient));

        // when
        PatientDto patientDto = patientsService.getPatientDto(patient.getUser().getEmail());

        // then
        assertAll(
                () -> assertEquals(patient.getFirstName(), patientDto.getFirstName()),
                () -> assertEquals(patient.getLastName(), patientDto.getLastName()),
                () -> assertEquals(patient.getId(), patientDto.getPatientId())
        );
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .user(user)
                .build();

        when(patientRepository.findByUserEmail(patient.getUser().getEmail())).thenReturn(Optional.of(patient));

        // when
        patientsService.deletePatient(patient.getUser().getEmail());

        // then
        verify(patientRepository).delete(patient);
    }

    @Test
    void editPatient_DataCorrect_PatientEdited() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .user(user)
                .build();

        UpdatePatientCommand updatePatientCommand = UpdatePatientCommand.builder()
                .idCardNo("cardNo2")
                .firstName("name2")
                .lastName("lastName2")
                .phoneNumber("number2")
                .birthday(LocalDate.of(2026, 1, 1))
                .build();

        when(patientRepository.findByUserEmail(patient.getUser().getEmail())).thenReturn(Optional.of(patient));

        // when
        PatientDto patientDto = patientsService.editPatient(user.getEmail(), updatePatientCommand);

        // then
        assertAll(
                () -> assertEquals("name2", patientDto.getFirstName()),
                () -> assertEquals("lastName2", patientDto.getLastName()),
                () -> assertEquals(LocalDate.of(2026, 1, 1), patientDto.getBirthday())
        );
    }

    @Test
    void getPatientDto_PatientNotFound_PatientNotFoundExceptionThrown() {
        // given
        when(patientRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        // when
        PatientNotFoundException exception = assertThrows(PatientNotFoundException.class,
                () -> patientsService.getPatientDto("email"));

        // then
        assertAll(
                () -> assertEquals("Patient wasn't found!", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }

    @Test
    void createPatient_PatientExists_PatientsEmailAlreadyExists() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        CreatePatientCommand command = CreatePatientCommand.builder()
                .email("email")
                .password("password")
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // when
        PatientsEmailAlreadyExists exception = assertThrows(PatientsEmailAlreadyExists.class,
                () -> patientsService.createPatient(command));

        // then
        assertAll(
                () -> assertEquals("User with this email already exists!", exception.getMessage()),
                () -> assertEquals(409, exception.getStatus().value())
        );
    }

    @Test
    void deletePatient_PatientExists_PatientDeleted() {
        // given
        User user = User.builder()
                .build();

        Patient patient = Patient.builder()
                .user(user)
                .build();

        when(patientRepository.findByUserEmail("email")).thenReturn(Optional.of(patient));

        // when
        patientsService.deletePatient("email");

        // then
        verify(patientRepository).findByUserEmail("email");
        verify(patientRepository).delete(patient);
        verifyNoMoreInteractions(patientRepository);
    }
}
