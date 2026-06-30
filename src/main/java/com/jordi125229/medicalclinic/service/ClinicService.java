package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.NoClinicException;
import com.jordi125229.medicalclinic.exception.NoDoctorException;
import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import com.jordi125229.medicalclinic.model.mapper.ClinicMapper;
import com.jordi125229.medicalclinic.repository.ClinicRepository;
import com.jordi125229.medicalclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;
    private final DoctorRepository doctorRepository;

    public PageableDto<ClinicDto> getClinics(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Clinic> pageClinics = clinicRepository.findAll(pageable);
        List<ClinicDto> clinics = pageClinics.stream()
                .map(clinicMapper::clinicToDto)
                .toList();
        return PageableDto.create(clinics, pageClinics);
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

    public Clinic getClinicById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(() -> new NoClinicException("Can't find clinic!", HttpStatus.NOT_FOUND));
    }

    private Clinic getClinicByName(String name) {
        return clinicRepository.findByName(name)
                .orElseThrow(() -> new NoClinicException("Can't find clinic!", HttpStatus.NOT_FOUND));
    }

    public ClinicDto assignDoctorToClinic(String email, Long id) {
        Doctor doctor = findDoctor(email);
        Clinic clinic = getClinicById(id);
        clinic.getDoctors().add(doctor);
        clinicRepository.save(clinic);
        return clinicMapper.clinicToDto(clinic);
    }

    public void deleteClinic(Long id) {
        clinicRepository.deleteById(id);
    }

    private Doctor findDoctor(String email) {
        return doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new NoDoctorException("Doctor wasn't found!", HttpStatus.NOT_FOUND));
    }
}
