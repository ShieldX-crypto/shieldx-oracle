package org.shieldx.oracle.api.dto;

import lombok.Builder;
import lombok.Getter;
import org.shieldx.oracle.api.dto.validator.ValidatorFilter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    public static<T> PageResponse<T> of(List<T> content, long total, ValidatorFilter filter) {
        int totalPages = (int) Math.ceil((double) total / filter.getSize());
        return PageResponse.<T>builder()
                .content(content)
                .page(filter.getPage())
                .size(filter.getSize())
                .totalElements(total)
                .totalPages(totalPages)
                .hasNext(filter.getPage() < totalPages - 1)
                .hasPrevious(filter.getPage() > 0)
                .build();
    }
}
