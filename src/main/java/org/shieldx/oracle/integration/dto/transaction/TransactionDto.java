package org.shieldx.oracle.integration.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDto {
    private String hash;
    private Long blockNum;
    private String sender;
    private Long nonce;
    private Long timestamp;
    private Long kAppFee;
    private Long bandwidthFee;
    private String status;
    private String resultCode;
    private Integer version;
    private String chainID;
    private Integer searchOrder;
}
