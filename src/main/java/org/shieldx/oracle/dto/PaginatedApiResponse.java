package org.shieldx.oracle.dto;

public class PaginatedApiResponse<T extends ApiData> extends ApiResponse<T> {
    private Pagination pagination;
}
