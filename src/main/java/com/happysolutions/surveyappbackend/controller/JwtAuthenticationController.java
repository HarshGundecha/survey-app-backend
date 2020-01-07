package com.happysolutions.surveyappbackend.controller;

import com.happysolutions.surveyappbackend.config.JwtTokenUtil;
import com.happysolutions.surveyappbackend.entity.User;
import com.happysolutions.surveyappbackend.model.JwtRequest;
import com.happysolutions.surveyappbackend.model.JwtResponse;
import com.happysolutions.surveyappbackend.service.JwtUserDetailsService;
import com.happysolutions.surveyappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getEmail());

		final String token = jwtTokenUtil.generateToken(userDetails);
//		System.out.println(userDetails.getUsername()+" is my name");
        User user = userService.getUserFromEmail(userDetails.getUsername());
        Map<String, Object> mymap = new HashMap();
        mymap.put("token",new JwtResponse(token).getToken());
        mymap.put("user",user);
        return ResponseEntity.ok(mymap);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}