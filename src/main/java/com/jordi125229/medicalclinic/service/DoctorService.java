package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoDoctorException;
import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.model.command.CreateDoctorCommand;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.DoctorMapper;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
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
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorMapper doctorMapper;

    public PageableDto<DoctorDto> getDoctors(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Doctor> doctorsPage = doctorRepository.findAll(pageable);
        List<DoctorDto> doctors = doctorsPage.stream()
                .map(doctorMapper::doctorToDto)
                .toList();
        return PageableDto.create(doctors, doctorsPage);
    }

    public DoctorDto getDoctorDto(String email) {
        Doctor doctor = getDoctorByEmail(email);
        return doctorMapper.doctorToDto(doctor);
    }

    private Doctor getDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new NoDoctorException("Doctor wasn't found!", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public DoctorDto createDoctor(CreateDoctorCommand commandDoctor) {
        User user = createUser(commandDoctor);
        userRepository.save(user);
        Doctor doctor = new Doctor(null, commandDoctor.getName(), commandDoctor.getLastName(),
                commandDoctor.getSpecialization(), null, user, null);
        doctorRepository.save(doctor);
        return doctorMapper.doctorToDto(doctor);
    }

    @Transactional
    private User createUser(CreateDoctorCommand commandDoctor) {
        validateEmail(commandDoctor.getEmail());
        User user = new User();
        user.setEmail(commandDoctor.getEmail());
        user.setPassword(commandDoctor.getPassword());
        return user;
    }

    @Transactional
    public void deleteDoctor(String email) {
        Doctor doctor = getDoctorByEmail(email);
        doctor.getUser().setDoctor(null);
        doctorRepository.deleteById(doctor.getId());
    }

    private void validateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new PatientsEmailAlreadyExists("User with this email already exists!", HttpStatus.CONFLICT);
        }
    }
}
