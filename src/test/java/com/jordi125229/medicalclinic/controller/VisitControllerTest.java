package com.jordi125229.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordi125229.medicalclinic.model.command.CreateVisitCommand;
import com.jordi125229.medicalclinic.model.dto.ClinicDto;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.VisitDto;
import com.jordi125229.medicalclinic.model.entity.Patient;
import com.jordi125229.medicalclinic.model.entity.Visit;
import com.jordi125229.medicalclinic.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private VisitService visitService;

    @Test
    void getVisits_DataCorrect_DataGotten() throws Exception {
        // given
        int size = 1;
        int page = 0;

        VisitDto visitDto = VisitDto.builder()
                .build();

        List<VisitDto> visitDtoList = List.of(visitDto);
        PageableDto<VisitDto> pageableDto = PageableDto.<VisitDto>builder()
                .pageSize(size)
                .pageNumber(page)
                .totalPages(1)
                .total(1L)
                .content(visitDtoList)
                .build();

        when(visitService.getVisits(page, size)).thenReturn(pageableDto);

        // when & then
        mockMvc.perform(get("/visits")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getVisit_DataCorrect_VisitGotten() throws Exception {
        // given
        VisitDto visitDto = VisitDto.builder()
                .doctorEmail("doctorEmail")
                .patientEmail("patientEmail")
                .build();

        when(visitService.getVisit(anyLong())).thenReturn(visitDto);

        // when & then
        mockMvc.perform(get("/visits/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorEmail").value("doctorEmail"))
                .andExpect(jsonPath("$.patientEmail").value("patientEmail"));
    }

    @Test
    void createVisit_DataCorrect_VisitCreated() throws Exception {
        // given
        VisitDto visitDto = VisitDto.builder()
                .doctorEmail("doctorEmail")
                .patientEmail("patientEmail")
                .build();

        CreateVisitCommand createVisitCommand = CreateVisitCommand.builder()
                .visitStart(LocalDateTime.of(2026, 7, 16, 15, 0))
                .visitEnd(LocalDateTime.of(2026, 7, 16, 15, 30))
                .clinicName("clinicName")
                .doctorEmail("doctorEmail@gmail.com")
                .patientEmail("patientEmail@gmail.com")
                .build();

        when(visitService.createVisit(any())).thenReturn(visitDto);

        // when & then
        mockMvc.perform(post("/visits")
                        .content(objectMapper.writeValueAsString(createVisitCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorEmail").value("doctorEmail"))
                .andExpect(jsonPath("$.patientEmail").value("patientEmail"));
    }

    @Test
    void assignPatientToVisit_DataCorrect_PatientAssigned() throws Exception {
        // given
        VisitDto visitDto = VisitDto.builder()
                .doctorEmail("doctorEmail")
                .patientEmail("patientEmail")
                .build();

        when(visitService.assignPatientToVisit(anyString(), anyString())).thenReturn(visitDto);

        // when & then
        mockMvc.perform(patch("/visits/email/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorEmail").value("doctorEmail"))
                .andExpect(jsonPath("$.patientEmail").value("patientEmail"));
    }

    @Test
    void deleteVisit_DataCorrect_VisitDeleted() throws Exception {
        mockMvc.perform(delete("/visits/id"))
                .andExpect(status().isOk());

        verify(visitService).deleteVisit("id");
    }
}
