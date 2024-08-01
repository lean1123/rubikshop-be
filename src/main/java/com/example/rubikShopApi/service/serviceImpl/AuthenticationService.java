package com.example.rubikShopApi.service.serviceImpl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.rubikShopApi.entity.InvalidToken;
import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.repository.InvalidTokenRepository;
import com.example.rubikShopApi.repository.UserRepository;
import com.example.rubikShopApi.request.IntroSpectRequest;
import com.example.rubikShopApi.request.LogoutTokenRequest;
import com.example.rubikShopApi.request.RefreshTokenRequest;
import com.example.rubikShopApi.response.AuthenticationResponse;
import com.example.rubikShopApi.response.IntroSpectResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	InvalidTokenRepository invalidTokenRepository;

	@NonFinal
	@Value("${jwt.SIGNER_KEY}")
	private String SIGNER_KEY;
	
	@NonFinal
	@Value("${jwt.expirationTime}")
	private int EXPIRATION_TIME;
	
	@NonFinal
	@Value("${jwt.refreshTime}")
	private int REFRESH_TIME;

	public AuthenticationResponse authenticate(String email) throws Exception {

		var user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("Error: Authenticate fail!"));

//        Giai ma mat khau

		var token = generateToken(user);

		System.out.println(token);

		return AuthenticationResponse.builder().token(token).authenticated(true).build();
	}

	public IntroSpectResponse introspect(IntroSpectRequest request) throws JOSEException, ParseException {
		var token = request.getToken();
		boolean isValid = true;

		try {
			verifyToken(token, false);
		} catch (Exception e) {
			isValid = false;
		}

		return IntroSpectResponse.builder().valid(isValid).build();
	}

	public String generateToken(User user) {
		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getEmail())
				.issuer(user.getFullName().replace("\\s+", "").toLowerCase() + ".com").issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString()).claim("scope", user.getRole()).build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void logoutToken(LogoutTokenRequest request) {
		try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidToken invalidatedToken =
                    InvalidToken.builder().tokenId(jit).invalidTime(new Date()).expiryTime(expiryTime).build();

            invalidTokenRepository.save(invalidatedToken);
        } catch (Exception exception) {
            log.info("Token already expired");
        }
	}
	
	public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws Exception {
		
		var signToken = verifyToken(request.getToken(), true);
		
		 String jit = signToken.getJWTClaimsSet().getJWTID();
         Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

         InvalidToken invalidatedToken =
                 InvalidToken.builder().tokenId(jit).invalidTime(new Date()).expiryTime(expiryTime).build();

         invalidTokenRepository.save(invalidatedToken);
         
         var email = signToken.getJWTClaimsSet().getSubject();
         
         var user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("Not existed user"));
         
         var refreshToken = generateToken(user);
         
         return AuthenticationResponse.builder().token(refreshToken).authenticated(true).build();
		
	}

	private SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {
		JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expiryTime = (isRefresh)
				? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESH_TIME, ChronoUnit.SECONDS)
						.toEpochMilli())
				: signedJWT.getJWTClaimsSet().getExpirationTime();

		var verified = signedJWT.verify(verifier);

		if (!(verified && expiryTime.after(new Date())))
			throw new Exception("Unauthented");

		if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
			throw new Exception("Unauthented");

		return signedJWT;
	}

}
