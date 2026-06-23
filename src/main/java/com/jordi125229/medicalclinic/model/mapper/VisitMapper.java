package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.model.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    @Mapping(target = "clinicId", source = "clinic.id")
    @Mapping(target = "clinicName", source = "clinic.name")
    @Mapping(target = "doctorEmail", source = "doctor.user.email")
    @Mapping(target = "patientEmail", source = "patient.user.email")
    VisitDto visitToDto(Visit visit);
}
