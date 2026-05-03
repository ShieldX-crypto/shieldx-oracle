package org.shieldx.oracle.util;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.shieldx.oracle.entity.CoveredEvent;
import org.shieldx.oracle.entity.Policy;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class PolicyUtilTest {
    static final byte[] PRECOMPUTED_HEX_SHA256 = "d3a41264c43325a2f9cd5676316c75018490788d1d51027711633088926bf038".getBytes(StandardCharsets.UTF_8);

    @Test
    void givenPolicy_encode_Sha256ShouldBeValid() {
        Policy policy = new Policy(
                "my_validator",
                CoveredEvent.JAILED,
                60,
                5000,
                120,
                1000,
                7,
                "0.0.1"
        );

        byte[] encoded = PolicyUtil.encode(policy);
        byte[] hash = PolicyUtil.hashEncoded(encoded);
        assertArrayEquals(PRECOMPUTED_HEX_SHA256, Hex.encode(hash));
    }
}