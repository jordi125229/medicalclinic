package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoClinicException;
import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.ClinicMapper;
import com.jordi125229.medicalclinic.model.mapper.DoctorMapper;
import com.jordi125229.medicalclinic.model.mapper.DoctorMapperImpl;
import com.jordi125229.medicalclinic.repository.ClinicRepository;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ClinicServiceTest {
    private ClinicRepository clinicRepository;
    private ClinicMapper clinicMapper;
    private DoctorRepository doctorRepository;
    private ClinicService clinicService;

    @BeforeEach
    void setUp() {
        this.clinicRepository = Mockito.mock(ClinicRepository.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.clinicMapper = Mappers.getMapper(ClinicMapper.class);
        ReflectionTestUtils.setField(clinicMapper, "doctorMapper", Mappers.getMapper(DoctorMapper.class));
        this.clinicService = new ClinicService(clinicRepository, clinicMapper, doctorRepository);
    }

    @Test
    void getClinics_DataCorrect_DataGotten() {
        // given
        int pageNumber = 0;
        int pageSize = 1;
        Clinic clinic = Clinic.builder()
                .name("name")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .build();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Clinic> clinics = List.of(clinic);
        Page<Clinic> pageClinic = new PageImpl<>(clinics, pageRequest, clinics.size());
        when(clinicRepository.findAll(pageRequest)).thenReturn(pageClinic);

        // when
        PageableDto<ClinicDto> pageClinicsDto = clinicService.getClinics(pageNumber, pageSize);

        // then
        assertAll(
                () -> assertEquals(pageNumber, pageClinicsDto.getPageNumber()),
                () -> assertEquals(pageSize, pageClinicsDto.getPageSize()),
                () -> assertEquals(clinics.size(), pageClinicsDto.getContent().size())
        );
    }

    @Test
    void createClinic_DataCorrect_ClinicCreated() {
        // given
        CreateClinicCommand createClinicCommand = CreateClinicCommand.builder()
                .name("name")
                .number("number")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .build();

        // when
        ClinicDto clinic = clinicService.createClinic(createClinicCommand);

        // then
        assertAll(
                () -> assertEquals("name", clinic.getName()),
                () -> assertEquals("number", clinic.getNumber()),
                () -> assertEquals("city", clinic.getCity()),
                () -> assertEquals("postalCode", clinic.getPostalCode()),
                () -> assertEquals("street", clinic.getStreet()),
                () -> assertEquals("number", clinic.getNumber())
        );
    }

    @Test
    void getClinicDto_DataCorrect_ClinicGotten() {
        // given
        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("name")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .build();
        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));

        // when
        ClinicDto clinicDto = clinicService.getClinicDto(1L);

        // then
        assertAll(
                () -> assertEquals(1L, clinicDto.getClinicId()),
                () -> assertEquals("name", clinicDto.getName()),
                () -> assertEquals("city", clinicDto.getCity()),
                () -> assertEquals("postalCode", clinicDto.getPostalCode()),
                () -> assertEquals("street", clinicDto.getStreet()),
                () -> assertEquals("number", clinicDto.getNumber())
        );

    }

    @Test
    void assignDoctorToClinic_DataCorrect_DoctorAssigned() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Doctor doctor = Doctor.builder()
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .build();

        Clinic clinic = Clinic.builder()
                .id(1L)
                .name("name")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .doctors(new HashSet<>())
                .build();

        when(clinicRepository.findById(1L)).thenReturn(Optional.of(clinic));
        when(doctorRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(doctor));

        // when
        ClinicDto clinicDto = clinicService.assignDoctorToClinic(doctor.getUser().getEmail(), 1L);

        // then
        assertAll(
                () -> assertEquals("email", clinicDto.getDoctorsDto().getFirst().getEmail())
        );
    }

    @Test
    void getClinicDto_ClinicNotFound_ExceptionThrown() {
        // given
        when(clinicRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        NoClinicException exception = assertThrows(NoClinicException.class, () -> clinicService.getClinicDto(1l));

        // then
        assertAll(
                () -> assertEquals("Can't find clinic!", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }

    @Test
    void deleteClinic_DataCorrect_ClinicDeleted() {
        // given
        Clinic clinic = Clinic.builder()
                .id(1L)
                .build();

        // when
        clinicService.deleteClinic(1L);

        // then
        verify(clinicRepository).deleteById(1L);
        verifyNoMoreInteractions(clinicRepository);
    }
}
