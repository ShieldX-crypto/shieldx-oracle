package org.shieldx.oracle.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.shieldx.oracle.exception.OracleSignatureException;
import org.shieldx.oracle.service.SignService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {
    @Value("${oracle.private-key-path}")
    private String privateKeyPath;

    private final ResourceLoader resourceLoader;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        log.trace("Init SignServiceImpl");
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
        log.trace("Load PrivateKey {}", privateKeyPath);
        this.privateKey = loadPrivateKey(privateKeyPath);
        log.trace("Load PrivateKey: Success.");
    }

    private PrivateKey loadPrivateKey(String location) {
        try {
            Resource resource = resourceLoader.getResource(location);

            try (Reader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream()));
                 PEMParser pemParser = new PEMParser(reader)) {

                Object object = pemParser.readObject();

                JcaPEMKeyConverter converter =
                        new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);

                if (object instanceof org.bouncycastle.asn1.pkcs.PrivateKeyInfo privateKeyInfo) {
                    return converter.getPrivateKey(privateKeyInfo);
                }

                if (object instanceof org.bouncycastle.openssl.PEMKeyPair pemKeyPair) {
                    return converter.getKeyPair(pemKeyPair).getPrivate();
                }

                throw new IllegalArgumentException("Unsupported PEM object: " + object.getClass().getName());
            }

        } catch (Exception e) {
            throw new IllegalStateException("Cannot read private key from " + location, e);
        }
    }

    @Override
    public byte[] sign(byte[] bytes) {
        try {
            Signature signature = Signature.getInstance("Ed25519");
            signature.initSign(privateKey);
            signature.update(bytes);
            return signature.sign();
        } catch (Exception e) {
            log.error("Failed to sign data", e);
            throw new OracleSignatureException("Failed to sign data", e);
        }
    }
}
