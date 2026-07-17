package com.jordi125229.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordi125229.medicalclinic.model.command.CreateDoctorCommand;
import com.jordi125229.medicalclinic.model.dto.DoctorDto;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private DoctorService doctorService;

    @Test
    void getDoctors_DataCorrect_DataGotten() throws Exception {
        // given
        int size = 1;
        int page = 0;

        DoctorDto doctorDto = DoctorDto.builder()
                .doctorId(1L)
                .build();

        List<DoctorDto> doctorDtoList = List.of(doctorDto);
        PageableDto<DoctorDto> pageableDto = PageableDto.<DoctorDto>builder()
                .pageSize(size)
                .pageNumber(page)
                .totalPages(1)
                .total(1)
                .content(doctorDtoList)
                .build();

        when(doctorService.getDoctors(page, size)).thenReturn(pageableDto);

        // when & then
        mockMvc.perform(get("/doctors")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void getDoctorByEmail_DataCorrect_DataGotten() throws Exception {
        // given
        DoctorDto doctorDto = DoctorDto.builder()
                .doctorId(1L)
                .email("email")
                .name("name")
                .build();
        when(doctorService.getDoctorDto("email")).thenReturn(doctorDto);

        // when & then
        mockMvc.perform(get("/doctors/email")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("email"))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void createDoctor_DataCorrect_DoctorCreated() throws Exception {
        // given
        CreateDoctorCommand createDoctorCommand = CreateDoctorCommand.builder()
                .email("email@gmail.com")
                .password("password")
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        DoctorDto doctor = DoctorDto.builder()
                .name("name")
                .lastName("lastName")
                .specialization("specialization")
                .build();

        when(doctorService.createDoctor(any())).thenReturn(doctor);

        // when & then
        mockMvc.perform(post("/doctors")
                        .content(objectMapper.writeValueAsString(createDoctorCommand))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.lastName").value("lastName"))
                .andExpect(jsonPath("$.specialization").value("specialization"));
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() throws Exception {
        mockMvc.perform(delete("/doctors/email"))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteDoctor("email");
    }
}
