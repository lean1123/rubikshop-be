package com.example.rubikShopApi.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.repository.InvalidTokenRepository;
import com.example.rubikShopApi.request.IntroSpectRequest;
import com.example.rubikShopApi.request.LogoutTokenRequest;
import com.example.rubikShopApi.request.RefreshTokenRequest;
import com.example.rubikShopApi.response.AuthenticationResponse;
import com.example.rubikShopApi.response.IntroSpectResponse;
import com.example.rubikShopApi.service.serviceImpl.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	AuthenticationService authenticationService;
	
	
	@PostMapping("introspect")
	public ResponseEntity<IntroSpectResponse> getIntroSpectToken(@RequestBody IntroSpectRequest request) throws JOSEException, ParseException {
		
		var result = authenticationService.introspect(request);
		
		return ResponseEntity.ok().body(new IntroSpectResponse(result.getValid()));
	}
	
	@PostMapping("logout")
	public ResponseEntity<Void> invalidatedToken(@RequestBody LogoutTokenRequest request){
		authenticationService.logoutToken(request);
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("refresh")
	public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws Exception{
		var result = authenticationService.refreshToken(request);
		
		return ResponseEntity.ok().body(result);
	}
}
