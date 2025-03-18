package com.github.konarjg.SWIFTCodeAPI.auth;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;
import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

    private SecretKey key;
    private String issuer;
    private JWSAlgorithm algorithm;

    @DurationMin(seconds = 1)
    private Duration expiresIn;

    private String apiKey;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Duration getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Duration expiresIn) {
        this.expiresIn = expiresIn;
    }

    public JWSAlgorithm getAlgorithm() {
        return algorithm;
    }

    public SecretKey getKey() {
        return key;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = JWSAlgorithm.parse(algorithm);
    }

    public void setKey(String key) {
        var jwk = new OctetSequenceKey.Builder(key.getBytes())
                .algorithm(algorithm)
                .build();

        this.key = jwk.toSecretKey();
        apiKey = key;
    }

    public String getApiKey() {
        return apiKey;
    }
}