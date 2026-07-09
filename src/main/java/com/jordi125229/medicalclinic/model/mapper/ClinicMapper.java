package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =  DoctorMapper.class)
public interface ClinicMapper {
    @Mapping(source = "id", target = "clinicId")
    @Mapping(source = "doctors", target = "doctorsDto")
    ClinicDto clinicToDto(Clinic clinic);
}
