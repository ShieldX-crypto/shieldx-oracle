package org.shieldx.oracle.integration.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginatedApiResponse<T extends ApiData> extends ApiResponse<T> {
    private Pagination pagination;
}
