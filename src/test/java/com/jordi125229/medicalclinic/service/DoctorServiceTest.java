package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoDoctorException;
import com.jordi125229.medicalclinic.model.command.CreateDoctorCommand;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.DoctorMapper;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DoctorServiceTest {

    private DoctorService doctorService;
    private DoctorMapper doctorMapper;
    private DoctorRepository doctorRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.doctorMapper = Mappers.getMapper(DoctorMapper.class);
        this.doctorService = new DoctorService(doctorRepository, userRepository, doctorMapper);
    }

    @Test
    void getDoctors_DataCorrect_DataGotten() {
        // given
        int pageNumber = 0;
        int pageSize = 1;
        Doctor doctor = Doctor.builder()
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<Doctor> doctors = List.of(doctor);
        Page<Doctor> pageDoctor = new PageImpl<>(doctors, pageRequest, doctors.size());
        when(doctorRepository.findAll(pageRequest)).thenReturn(pageDoctor);

        // when
        PageableDto<DoctorDto> pageDoctorsDto = doctorService.getDoctors(pageNumber, pageSize);

        // then
        assertAll(
                () -> assertEquals(pageNumber, pageDoctorsDto.getPageNumber()),
                () -> assertEquals(pageSize, pageDoctorsDto.getPageSize()),
                () -> assertEquals(doctors.size(), pageDoctorsDto.getContent().size())
        );
    }

    @Test
    void getDoctorDto_DataCorrect_DoctorDtoGotten() {
        //given
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
        when(doctorRepository.findByUserEmail(user.getEmail())).thenReturn(Optional.of(doctor));

        // when
        DoctorDto doctorDto = doctorService.getDoctorDto(doctor.getUser().getEmail());

        // then
        assertAll(
                () -> assertEquals(doctor.getUser().getEmail(), doctorDto.getEmail()),
                () -> assertEquals(doctor.getName(), doctorDto.getName()),
                () -> assertEquals(doctor.getLastName(), doctorDto.getLastName())
        );
    }

    @Test
    void createDoctor_DataCorrect_DoctorCreated() {
        // given
        CreateDoctorCommand createDoctorCommand = CreateDoctorCommand.builder()
                .email("email")
                .password("password")
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        when(doctorRepository.findByUserEmail(createDoctorCommand.getEmail())).thenReturn(Optional.empty());

        // when
        DoctorDto doctorDto = doctorService.createDoctor(createDoctorCommand);

        // then
        assertAll(
                () -> assertEquals("email", doctorDto.getEmail()),
                () -> assertEquals("name", doctorDto.getName()),
                () -> assertEquals("lastName", doctorDto.getLastName()),
                () -> assertEquals("specialization", doctorDto.getSpecialization())
        );
    }

    @Test
    void getDoctorDto_DoctorNotFound_NoDoctorException() {
        // given
        when(doctorRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        // when
        NoDoctorException exception = assertThrows(NoDoctorException.class,
                () -> doctorService.getDoctorDto("email"));

        // then
        assertAll(
                () -> assertEquals("Doctor wasn't found!", exception.getMessage()),
                () -> assertEquals(404, exception.getStatus().value())
        );
    }

    @Test
    void deleteDoctor_DataCorrect_DoctorDeleted() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .user(user)
                .build();

        when(doctorRepository.findByUserEmail("email")).thenReturn(Optional.of(doctor));

        // when
        doctorService.deleteDoctor("email");

        // then
        verify(doctorRepository).deleteById(1L);
    }
}
