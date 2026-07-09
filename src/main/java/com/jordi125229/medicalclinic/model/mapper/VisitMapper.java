package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =  ClinicMapper.class)
public interface VisitMapper {

    @Mapping(target = "clinicDto", source = "clinic")
    @Mapping(target = "doctorEmail", source = "doctor.user.email")
    @Mapping(target = "patientEmail", source = "patient.user.email")
    VisitDto visitToDto(Visit visit);
}
