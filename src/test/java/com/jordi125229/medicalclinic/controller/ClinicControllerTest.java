package com.jordi125229.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordi125229.medicalclinic.model.command.CreateClinicCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.entity.Clinic;
import com.jordi125229.medicalclinic.service.ClinicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClinicControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ClinicService clinicService;

    @Test
    void getClinics_DataCorrect_DataGotten() throws Exception {
        // given
        int size = 1;
        int page = 0;

        ClinicDto clinicDto = ClinicDto.builder()
                .clinicId(1L)
                .build();

        List<ClinicDto> clinicDtoList = List.of(clinicDto);
        PageableDto<ClinicDto> pageableDto = PageableDto.<ClinicDto>builder()
                .pageSize(size)
                .pageNumber(page)
                .totalPages(1)
                .total(1L)
                .content(clinicDtoList)
                .build();

        when(clinicService.getClinics(page, size)).thenReturn(pageableDto);

        // when & then
        mockMvc.perform(get("/clinics")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getClinicById_DataCorrect_DataGotten() throws Exception {
        // given
        ClinicDto clinicDto = ClinicDto.builder()
                .clinicId(1L)
                .name("name")
                .build();

        when(clinicService.getClinicDto(any())).thenReturn(clinicDto);

        // when & then
        mockMvc.perform(get("/clinics/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clinicId").value(1L))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void createClinic_DataCorrect_ClinicCreated() throws Exception {
        // given
        ClinicDto clinicDto = ClinicDto.builder()
                .clinicId(1L)
                .name("name")
                .number("number")
                .city("city")
                .build();

        CreateClinicCommand createClinicCommand = CreateClinicCommand.builder()
                .name("name")
                .number("number")
                .city("city")
                .postalCode("postalCode")
                .street("street")
                .number("number")
                .build();

        when(clinicService.createClinic(any())).thenReturn(clinicDto);

        // when & then
        mockMvc.perform(post("/clinics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createClinicCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.number").value("number"))
                .andExpect(jsonPath("$.city").value("city"));
    }

    @Test
    void assignDoctorToClinic_DataCorrect_DoctorAssigned() throws Exception {
        // given
        DoctorDto doctor = DoctorDto.builder()
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        ClinicDto clinicDto = ClinicDto.builder()
                .clinicId(1L)
                .name("name")
                .number("number")
                .city("city")
                .doctorsDto(List.of(doctor))
                .build();

        when(clinicService.assignDoctorToClinic(anyString(), anyLong())).thenReturn(clinicDto);

        // when & then
        mockMvc.perform(patch("/clinics/email/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.doctorsDto[0].name").value("name"))
                .andExpect(jsonPath("$.doctorsDto[0].lastName").value("lastName"))
                .andExpect(jsonPath("$.doctorsDto[0].specialization").value("specialization"));
    }

    @Test
    void deleteClinic_DataCorrect_ClinicDeleted() throws Exception {
        mockMvc.perform(delete("/clinics/1"))
                .andExpect(status().isNoContent());

        verify(clinicService).deleteClinic(1L);
    }
}
