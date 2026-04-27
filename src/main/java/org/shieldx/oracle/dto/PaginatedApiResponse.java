package org.shieldx.oracle.dto;

import lombok.Data;

@Data
public class PaginatedApiResponse<T extends ApiData> extends ApiResponse<T> {
    private Pagination pagination;
}
