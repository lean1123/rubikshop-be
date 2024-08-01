package com.example.rubikShopApi.controller.admin;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.entity.Role;
import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.request.LoginForm;
import com.example.rubikShopApi.request.RegisterForm;
import com.example.rubikShopApi.request.ResponseObject;
import com.example.rubikShopApi.response.AuthenticationResponse;
import com.example.rubikShopApi.service.IUserService;
import com.example.rubikShopApi.service.serviceImpl.AuthenticationService;

@RestController
@RequestMapping("/admin")
public class UserControllerForAdmin {

	@Autowired
	IUserService userService;

	@Autowired
	AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<ResponseObject> register(@RequestBody RegisterForm registerForm, BindingResult result) {

		if (result.hasErrors()) {
			System.out.println(result.getErrorCount());
			return ResponseEntity.badRequest().body(new ResponseObject(null, "INSERT FAIL", "Fail", null));
		}
		
		Optional<User> opt = userService.findByEmail(registerForm.getEmail());

		if (opt.isPresent()) {
			return ResponseEntity.badRequest().body(new ResponseObject(null, "INSERT FAIL", "Existed User!", null));
		}

		if (registerForm.getIsOauth2() != true) {
			if (!registerForm.getPassword().equalsIgnoreCase(registerForm.getRetypePassword())) {
				return ResponseEntity.badRequest()
						.body(new ResponseObject(null, "INSERT FAIL", "Retype password invalid!", null));
			}

		}

		User user = new User();

		BeanUtils.copyProperties(registerForm, user);

		user.setRole(Role.ADMIN);

		userService.save(user);

		return ResponseEntity.ok().body(new ResponseObject(null, "Insert oke", "A new Admin has been saved", user));

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
		Optional<User> opt = userService.findByEmail(loginForm.getEmail());
		if (opt.isPresent()) {
			User user = opt.get();

			if (!user.getRole().equals(Role.ADMIN)) {
				return ResponseEntity.badRequest().body("This page for admin");
			}

			if (loginForm.getIsOauth2() == true) {
				return ResponseEntity.ok().body(new ResponseObject(null, "OK", "A user was logged", user));
			}

			if (loginForm.getPassword().equalsIgnoreCase(user.getPassword())) {
				AuthenticationResponse authRes;
				try {
					authRes = authenticationService.authenticate(loginForm.getEmail());

					if (StringUtils.hasText(authRes.getToken())) {

						return ResponseEntity.ok().body(
								new ResponseObject(authRes.getToken().toString(), "OK", "A user was logged", user));
					} else {
						return ResponseEntity.badRequest()
								.body(new ResponseObject(null, "Authenticated fail", "Not authenticated!", null));
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
				}
			} else {

				return ResponseEntity.badRequest()
						.body(new ResponseObject(null, "LOGIN FAILURE", "Invalid password", null));

			}
		}
		return ResponseEntity.badRequest().body("User isn't sign up in system!");
	}
	
	@GetMapping("/users")
	public ResponseEntity<?> getAllUser(){
		
		var result = userService.findAll();
		
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/users/myInfo")
	public ResponseEntity<?> getMyInfo(){
		var result = userService.getMyInfo();
		
		return ResponseEntity.ok().body(result);
	}

}
