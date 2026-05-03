package org.shieldx.oracle.service.impl;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.shieldx.oracle.entity.CoveredEvent;
import org.shieldx.oracle.entity.Policy;
import org.shieldx.oracle.service.SignService;
import org.shieldx.oracle.util.PolicyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SignServiceImplTest {
    static final byte[] PRECOMPUTED_ORACLE_SIGNATURE_HEX = (
            "8994dd9025a15cbb348d8915888165f37a46e89a03caf453531394495c7bf219" +
                    "eb4064cdb05a16dfa0b5a3afdf6f63ba17e1f487f90cb0684efe3b2dc1983007").getBytes(StandardCharsets.UTF_8);

    Policy policy;
    byte[] hashedPolicy;
    @Autowired
    SignService signService;

    @BeforeEach
    void setUp() {
        policy = new Policy(
                "my_validator",
                CoveredEvent.JAILED,
                10,
                2000,
                60,
                1000,
                8,
                "0.0.1"
        );
        byte[] encoded = PolicyUtil.encode(policy);
        hashedPolicy = PolicyUtil.hashEncoded(encoded);
    }

    @Test
    void sign() {
        byte[] signature = assertDoesNotThrow(() -> signService.sign(hashedPolicy));
        assertNotNull(signature);
        assertArrayEquals(PRECOMPUTED_ORACLE_SIGNATURE_HEX, Hex.encode(signature));
    }
}