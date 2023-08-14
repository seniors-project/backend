package com.seniors.common.dto;

import com.seniors.domain.resume.dto.ResumeDto;
import com.seniors.domain.resume.entity.Resume;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomSlice<T> {
    private List<T> content;
    private Pageable pageable;
    private boolean last;
    private long lastId;
    private boolean first;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean empty;

    public static <T> CustomSlice<T> from(Slice<T> slice) {
        if (slice == null) return null;
        CustomSlice<T> response = new CustomSlice<>();
        response.setContent(slice.getContent());
        response.setPageable(slice.getPageable());
        response.setLast(slice.isLast());
        response.setFirst(slice.isFirst());
        response.setSize(slice.getSize());
        response.setNumber(slice.getNumber());
        response.setSort(slice.getSort());
        response.setNumberOfElements(slice.getNumberOfElements());
        response.setEmpty(slice.isEmpty());

        if(slice.getNumberOfElements()!=0) {
            T lastItem = slice.getContent().get(slice.getContent().size() - 1);
            if (lastItem instanceof ResumeDto.GetResumeByQueryDslRes) {
                Long lastId = ((ResumeDto.GetResumeByQueryDslRes) lastItem).getId();
                response.setLastId(lastId);
            }
        }

        return response;

    }
}
