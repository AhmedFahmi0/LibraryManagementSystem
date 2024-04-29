package com.libraryManagement.libraryManagement.User.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.libraryManagement.libraryManagement.User.entities.User;
import com.libraryManagement.libraryManagement.User.model.request.AuthenticationRequestModel;
import com.libraryManagement.libraryManagement.User.model.response.AuthenticationResModel;
import com.libraryManagement.libraryManagement.User.repositories.UserRepository;
import com.libraryManagement.libraryManagement.securityConfig.JwtService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    
    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User userCred = repository.findByUsername(username)!=null?repository.findByUsername(username).get():null;
        if (userCred == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
            userCred.getUsername(), userCred.getPassword(), new ArrayList<>());
    }
    

      public AuthenticationResModel authenticate(AuthenticationRequestModel request) {
    	   authenticationManager.authenticate(
    		        new UsernamePasswordAuthenticationToken(
    		            request.getUserName(),
    		            request.getPassword()
    		        )
    		    );
    		    User user = repository.findByUsername(request.getUserName())
    		        .orElseThrow();
    		    var jwtToken = jwtService.generateToken(user);
    		    return AuthenticationResModel.builder()
    		    		 .accessToken(jwtToken)
    		        .build();
      }

	public AuthenticationResModel register(AuthenticationRequestModel request) {
		 User user = User.builder()
			        .username(request.getUserName())
			        .password(passwordEncoder.encode(request.getPassword()))
			        .build();
			    repository.save(user);
			    var jwtToken = jwtService.generateToken(user);
			    return AuthenticationResModel.builder()
			        .accessToken(jwtToken)
			        .build();
	}
}
