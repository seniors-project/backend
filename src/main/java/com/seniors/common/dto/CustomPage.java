package com.seniors.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> list;
    private Pageable pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean empty;

    public static <T> CustomPage<T> of(Page<T> page) {
        if (page == null) return null;
        List<T> list = page.getContent();
        CustomPage<T> response = new CustomPage<>();
        response.setList(list);
        response.setPageable(page.getPageable());
        response.setLast(page.isLast());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setFirst(page.isFirst());
        response.setSize(page.getSize());
        response.setNumber(page.getNumber());
        response.setSort(page.getSort());
        response.setNumberOfElements(page.getNumberOfElements());
        response.setEmpty(page.isEmpty());
        return response;
    }
}
