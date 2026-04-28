package org.shieldx.oracle.integration.dto;

import lombok.Data;

@Data
public class Pagination {
    private int self;
    private int next;
    private int previous;
    private int perPage;
    private int totalPages;
    private int totalRecords;
}
