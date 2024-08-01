package com.example.rubikShopApi.config;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.rubikShopApi.request.IntroSpectRequest;
import com.example.rubikShopApi.service.serviceImpl.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.experimental.NonFinal;

@Component
public class CustomJwtDecoder implements JwtDecoder {

	@NonFinal
	@Value("${jwt.SIGNER_KEY}")
	private String SIGNER_KEY;

	@Autowired
	private AuthenticationService authenticationService;

	private NimbusJwtDecoder nimbusJwtDecoder = null;

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
            var response = authenticationService.introspect(
                    IntroSpectRequest.builder().token(token).build());

            if (!response.getValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
	}

}
