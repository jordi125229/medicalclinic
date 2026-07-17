package com.jordi125229.medicalclinic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordi125229.medicalclinic.model.command.CreatePatientCommand;
import com.jordi125229.medicalclinic.model.command.UpdatePatientCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.PatientDto;
import com.jordi125229.medicalclinic.service.PatientsService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PatientsService patientsService;

    @Test
    void getPatients_DataCorrect_DataGotten() throws Exception {
        // given
        int size = 1;
        int page = 0;

        PatientDto patient = PatientDto.builder()
                .patientId(1L)
                .build();

        List<PatientDto> patientsDto = List.of(patient);
        PageableDto<PatientDto> patientDtoPage = PageableDto.<PatientDto>builder()
                .pageSize(size)
                .pageNumber(page)
                .totalPages(1)
                .content(patientsDto)
                .total(1)
                .build();

        when(patientsService.getPatients(page, size)).thenReturn(patientDtoPage);

        mockMvc.perform(get("/patients")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.content[0].patientId").value(1));

        verify(patientsService).getPatients(page, size);
    }

    @Test
    void getPatientByEmail_DataCorrect_PatientGotten() throws Exception {
        // given
        PatientDto patient = PatientDto.builder()
                .patientId(1L)
                .email("email")
                .firstName("name")
                .lastName("lastName")
                .build();

        when(patientsService.getPatientDto(patient.getEmail())).thenReturn(patient);

        // when & then
        mockMvc.perform(get("/patients/email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("email"))
                .andExpect(jsonPath("$.firstName").value("name"))
                .andExpect(jsonPath("$.lastName").value("lastName"));

        verify(patientsService).getPatientDto(patient.getEmail());
    }

    @Test
    void createPatient_DataCorrect_PatientCreated() throws Exception {
        // given
        CreatePatientCommand command = CreatePatientCommand.builder()
                .email("email@gmail.com")
                .password("password")
                .idCardNo("idCardNo")
                .firstName("name")
                .lastName("lastName")
                .phoneNumber("12345678")
                .birthday(LocalDate.of(2020,1,1))
                .build();

        PatientDto patientDto = PatientDto.builder()
                .firstName("name")
                .lastName("lastName")
                .email("email")
                .build();

        when(patientsService.createPatient(any())).thenReturn(patientDto);

        // when & then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(jsonPath("$.firstName").value("name"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.email").value("email"));
    }

    @Test
    void updatePatient_DataCorrect_PatientCreated() throws Exception {
        UpdatePatientCommand updatePatientCommand = UpdatePatientCommand.builder()
                .firstName("newName")
                .lastName("newLastName")
                .build();

        PatientDto patientDto = PatientDto.builder()
                .patientId(1L)
                .email("email")
                .firstName("newName")
                .lastName("newLastName")
                .build();

        when(patientsService.editPatient(eq("email"), any())).thenReturn(patientDto);

        mockMvc.perform(put("/patients/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePatientCommand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.email").value("email"))
                .andExpect(jsonPath("$.firstName").value("newName"))
                .andExpect(jsonPath("$.lastName").value("newLastName"));
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() throws Exception {
        mockMvc.perform(delete("/patients/email"))
                .andExpect(status().isNoContent());

        verify(patientsService).deletePatient("email");
    }
}
