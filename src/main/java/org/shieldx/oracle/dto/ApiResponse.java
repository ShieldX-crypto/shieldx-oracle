package org.shieldx.oracle.dto;

import lombok.Data;

@Data
public class ApiResponse<T extends ApiData> {
    T data;
    String error;
    String code;
}
