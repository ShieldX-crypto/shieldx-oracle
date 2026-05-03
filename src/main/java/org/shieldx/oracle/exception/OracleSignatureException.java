package org.shieldx.oracle.exception;

public class OracleSignatureException extends RuntimeException {
    public OracleSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public OracleSignatureException(Throwable cause) {
        super(cause);
    }
}
