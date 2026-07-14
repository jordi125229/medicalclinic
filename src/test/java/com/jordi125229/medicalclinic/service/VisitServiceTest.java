package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoVisitException;
import com.jordi125229.medicalclinic.model.command.CreateVisitCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.model.entity.*;
import com.jordi125229.medicalclinic.model.mapper.ClinicMapper;
import com.jordi125229.medicalclinic.model.mapper.VisitMapper;
import com.jordi125229.medicalclinic.repository.ClinicRepository;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VisitServiceTest {
    private VisitRepository visitRepository;
    private VisitMapper visitMapper;
    private ClinicRepository clinicRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private VisitService visitService;

    @BeforeEach
    void setUp() {
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        ReflectionTestUtils.setField(visitMapper, "clinicMapper", Mappers.getMapper(ClinicMapper.class));
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitService = new VisitService(visitRepository, visitMapper, clinicRepository, doctorRepository, patientRepository);
    }

    @Test
    void getVisits_DataCorrect_DataGotten() {
        // given
        int pageNumber = 0;
        int pageSize = 1;
        Visit visit = Visit.builder()
                .id(1L)
                .visitStart(LocalDateTime.of(2025, 12, 4, 10, 0))
                .visitEnd(LocalDateTime.of(2025, 12, 4, 10, 30))
                .build();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Visit> visits = List.of(visit);
        Page<Visit> pageVisit = new PageImpl<>(visits, pageRequest, visits.size());
        when(visitRepository.findAll(pageRequest)).thenReturn(pageVisit);

        // when
        PageableDto<VisitDto> visitsDtoPage = visitService.getVisits(pageNumber, pageSize);

        // then
        assertAll(
                () -> assertEquals(pageNumber, visitsDtoPage.getPageNumber()),
                () -> assertEquals(pageSize, visitsDtoPage.getPageSize()),
                () -> assertEquals(visits.size(), visitsDtoPage.getContent().size())
        );
    }

    @Test
    void createVisit_DataCorrect_VisitCreated() {
        // given
        CreateVisitCommand command = CreateVisitCommand.builder()
                .clinicName("clinicName")
                .doctorEmail("doctorEmail")
                .visitStart(LocalDateTime.of(2025, 12, 4, 10, 0))
                .visitEnd(LocalDateTime.of(2025, 12, 4, 10, 30))
                .patientEmail("patientEmail")
                .build();

        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("name")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .build();

        Doctor doctor = Doctor.builder()
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        Patient patient = Patient.builder()
                .idCardNo("cardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("number")
                .birthday(LocalDate.of(2025, 12, 1))
                .build();

        when(clinicRepository.findByName(command.getClinicName())).thenReturn(Optional.of(clinic));
        when(doctorRepository.findByUserEmail(command.getDoctorEmail())).thenReturn(Optional.of(doctor));
        when(patientRepository.findByUserEmail(command.getPatientEmail())).thenReturn(Optional.of(patient));
        when(visitRepository.existsByDoctorAndVisitStartLessThanAndVisitEndGreaterThan(any(), any(), any())).thenReturn(false);

        // when
        VisitDto resultVisit = visitService.createVisit(command);

        // then
        assertAll(
                () -> assertEquals(clinic.getId(), resultVisit.getClinicDto().getClinicId()),
                () -> assertNotNull(resultVisit)
        );
    }

    @Test
    void assignPatientToVisit_DataCorrect_PatientAssigned() {
        // given
        Visit visit = Visit.builder()
                .id(1L)
                .visitStart(LocalDateTime.of(2025, 12, 4, 10, 0))
                .visitEnd(LocalDateTime.of(2025, 12, 4, 10, 30))
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

        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));
        when(patientRepository.findByUserEmail(anyString())).thenReturn(Optional.of(patient));

        // when
        VisitDto visitResult = visitService.assignPatientToVisit("email", "1");

        // then
        assertAll(
                () -> assertEquals(patient, visit.getPatient()),
                () -> assertEquals("email", visitResult.getPatientEmail())
        );
    }

    @Test
    void deleteVisit_DataCorrect_VisitDeleted() {
        // given
        Visit visit = Visit.builder()
                .id(1L)
                .build();

        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));

        // when
        visitService.deleteVisit("1");

        // then
        verify(visitRepository).delete(visit);
    }

    @Test
    void getVisit_VisitNotFound_ExceptionThrown() {
        // given
        when(visitRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        NoVisitException exception = assertThrows(NoVisitException.class, () -> visitService.getVisit(1L));

        // then
        assertAll(
                () -> assertEquals("Can't find visit", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }
}
