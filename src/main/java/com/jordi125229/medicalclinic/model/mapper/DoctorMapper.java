package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "id", target = "doctorId")
    @Mapping(source = "user.email", target = "email")
    DoctorDto doctorToDto(Doctor doctor);
}
