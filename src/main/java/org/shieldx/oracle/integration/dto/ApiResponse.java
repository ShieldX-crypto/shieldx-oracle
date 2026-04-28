package org.shieldx.oracle.integration.dto;

import lombok.Data;

@Data
public class ApiResponse<T extends ApiData> {
    T data;
    String error;
    String code;
}
