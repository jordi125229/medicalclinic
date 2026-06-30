package com.jordi125229.medicalclinic.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class PageableDto<T> {
    private List<T> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer total;
    private Integer totalPages;

    public static <T> PageableDto<T> create(List<T> content, Page page) {
        return new PageableDto<>(content, page.getSize(), page.getPageable().getPageNumber(), page.getTotalPages(),
                page.getTotalPages());
    }
}
