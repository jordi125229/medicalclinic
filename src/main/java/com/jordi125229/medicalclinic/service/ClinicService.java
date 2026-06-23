package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoClinicException;
import com.jordi125229.medicalclinic.exception.NoDoctorException;
import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.mapper.ClinicMapper;
import com.jordi125229.medicalclinic.repository.ClinicRepository;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;
    private final DoctorRepository doctorRepository;

    public List<ClinicDto> getClinics() {
        return clinicRepository.findAll().stream()
                .map(clinicMapper::clinicToDto)
                .toList();
    }

    public ClinicDto createClinic(CreateClinicCommand clinicCommand) {
        Clinic clinic = new Clinic(null, clinicCommand.getName(), clinicCommand.getCity(),
                clinicCommand.getPostalCode(), clinicCommand.getStreet(), clinicCommand.getNumber(), null, null);
        clinicRepository.save(clinic);
        return clinicMapper.clinicToDto(clinic);
    }

    public ClinicDto getClinicDto(String name) {
        Clinic clinic = getClinicByName(name);
        return clinicMapper.clinicToDto(clinic);
    }

    public Clinic getClinicByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new NoClinicException("Can't find clinic!", HttpStatus.NOT_FOUND));
    }

    public void assignDoctorToClinic(String email, String name) {
        Doctor doctor = findDoctor(email);
        Clinic clinic = getClinicByName(name);
        clinic.getDoctors().add(doctor);
        clinicRepository.save(clinic);
    }

    public void deleteClinic(String name) {
        Clinic clinic = getClinicByName(name);
        clinicRepository.delete(clinic);
    }

    private Doctor findDoctor(String email) {
        return doctorRepository.findByUserEmail(email).orElseThrow(() -> new NoDoctorException("Doctor wasn't found!", HttpStatus.NOT_FOUND));
    }
}
