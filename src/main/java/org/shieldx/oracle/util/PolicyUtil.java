package org.shieldx.oracle.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.shieldx.oracle.entity.Policy;
import org.shieldx.oracle.exception.OracleSignatureException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
@UtilityClass
public class PolicyUtil {

    public byte[] hashEncoded(byte[] encoded) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(encoded);
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not available", e);
            throw new OracleSignatureException(e);
        }
    }

    public byte[] encode(Policy policy) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // subject_key (просто bytes)
            out.write(policy.getSubjectKey().getBytes(StandardCharsets.UTF_8));

            // covered_event как u64 (8 байт!)
            out.write(longToBytes(policy.getCoveredEvent().ordinal()));

            // duration_epochs (u64)
            out.write(longToBytes(policy.getDurationEpochs()));

            // payout (BigUint без длины)
            out.write(toUnsignedBytes(policy.getPayout()));

            // premium (BigUint без длины)
            out.write(toUnsignedBytes(policy.getPremium()));

            // expiry (u64)
            out.write(longToBytes(policy.getExpiry()));

            // nonce (u64)
            out.write(longToBytes(policy.getNonce()));

            // risk_model_version (просто bytes)
            out.write(policy.getRiskModelVersion().getBytes(StandardCharsets.UTF_8));

            return out.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException("Encoding failed", e);
        }
    }

    private byte[] longToBytes(long value) {
        return ByteBuffer.allocate(Long.BYTES)
                .putLong(value)
                .array(); // big-endian по умолчанию
    }

    private byte[] toUnsignedBytes(long value) {
        return toUnsignedBytes(BigInteger.valueOf(value));
    }

    private byte[] toUnsignedBytes(BigInteger value) {
        if (value.signum() < 0) {
            throw new IllegalArgumentException("Negative BigUint");
        }

        byte[] bytes = value.toByteArray();

        // Убираем sign byte (0x00)
        if (bytes.length > 1 && bytes[0] == 0x00) {
            return Arrays.copyOfRange(bytes, 1, bytes.length);
        }

        return bytes;
    }
}
