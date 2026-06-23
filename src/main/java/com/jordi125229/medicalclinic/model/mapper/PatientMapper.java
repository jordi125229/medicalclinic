package com.jordi125229.medicalclinic.model.mapper;

import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.model.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(source = "id", target = "patientId")
    @Mapping(source = "user.email", target = "email")
    PatientDto patientToDto (Patient patient);
}
