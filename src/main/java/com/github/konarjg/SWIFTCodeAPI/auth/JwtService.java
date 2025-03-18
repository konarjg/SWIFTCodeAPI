package com.github.konarjg.SWIFTCodeAPI.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {

    private final AppJwtProperties appJwtProperties;

    public JwtService(AppJwtProperties appJwtProperties) {
        this.appJwtProperties = appJwtProperties;
    }

    public String generateJWT(Map<String, Object> claims) {
        var key = appJwtProperties.getKey();
        var algorithm = appJwtProperties.getAlgorithm();

        var header = new JWSHeader(algorithm);
        var claimsSet = buildClaimsSet(claims);

        var jwt = new SignedJWT(header, claimsSet);

        try {
            var signer = new MACSigner(key);
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Unable to generate JWT", e);
        }

        return jwt.serialize();
    }

    private JWTClaimsSet buildClaimsSet(Map<String, Object> claims) {
        if (!claims.containsKey("scope")) {
            return null;
        }

        List<?> scope = (List<?>)claims.get("scope");

        if (scope.contains("ADMIN")) {
            if (!claims.containsKey("key")) {
                return null;
            }

            if (!claims.get("key").equals(appJwtProperties.getApiKey())) {
                return null;
            }
        }

        var issuer = appJwtProperties.getIssuer();
        var issuedAt = Instant.now();
        var expirationTime = issuedAt.plus(appJwtProperties.getExpiresIn());

        var builder = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expirationTime));

        claims.forEach(builder::claim);

        return builder.build();
    }

}
