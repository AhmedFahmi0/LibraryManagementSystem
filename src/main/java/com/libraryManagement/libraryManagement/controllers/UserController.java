package com.libraryManagement.libraryManagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryManagement.libraryManagement.model.request.AuthenticationRequestModel;
import com.libraryManagement.libraryManagement.model.response.AuthenticationResModel;
import com.libraryManagement.libraryManagement.services.serviceImpl.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
	@Autowired
	 UserServiceImpl userService;
	
	  @ApiOperation("create a new user")
	  @PostMapping("/register")
	  public ResponseEntity<AuthenticationResModel> register(
	      @RequestBody AuthenticationRequestModel request
	  ) {
	    return ResponseEntity.ok(userService.register(request));
	  }
	  
	  @ApiOperation("authenticate a user")
	  @PostMapping("/authenticate")
	  public ResponseEntity<AuthenticationResModel> authenticate(
	      @RequestBody AuthenticationRequestModel request
	  ) {
	    return ResponseEntity.ok(userService.authenticate(request));
	  }

}
