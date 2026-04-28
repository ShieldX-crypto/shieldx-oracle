package org.shieldx.oracle.integration.dto.transaction;

import lombok.Data;
import org.shieldx.oracle.integration.dto.ApiData;

import java.util.List;

@Data
public class TransactionListDataWrapper implements ApiData {
    private List<TransactionDto> transactions;
}
