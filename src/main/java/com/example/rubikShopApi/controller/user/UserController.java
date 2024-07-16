package com.example.rubikShopApi.controller.user;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rubikShopApi.entity.User;
import com.example.rubikShopApi.request.LoginForm;
import com.example.rubikShopApi.request.RegisterForm;
import com.example.rubikShopApi.request.ResponseObject;
import com.example.rubikShopApi.service.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	IUserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ResponseObject> register(@RequestBody @Valid RegisterForm registerForm, BindingResult result){
		
		if(result.hasErrors()) {
			System.out.println(result.getErrorCount());
			return ResponseEntity.badRequest().body(new ResponseObject("INSERT FAIL", "Fail", null));
		}
		
		if(!registerForm.getPassword().equalsIgnoreCase(registerForm.getRetypePassword())) {
			return ResponseEntity.badRequest().body(new ResponseObject("INSERT FAIL", "Retype password invalid!", null));
		}
		
		User user = new User();
		
		BeanUtils.copyProperties(registerForm, user);
		
		userService.save(user);
		
		return ResponseEntity.ok().body(new ResponseObject("Insert oke", "A new user has been saved", user)); 

	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseObject> login(@RequestBody LoginForm loginForm){
		Optional<User> opt = userService.findByEmail(loginForm.getEmail());
		if(opt.isPresent()) {
			User user= opt.get();
			if(loginForm.getPassword().equalsIgnoreCase(user.getPassword()))
				return ResponseEntity.ok().body(new ResponseObject("OK", "A user was logged", user));
			else
				return ResponseEntity.badRequest().body(new ResponseObject("LOGIN FAILURE", "Invalid password", null));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("LOGIN FAILURE", "Not found user by email", null));
	}
	
}
