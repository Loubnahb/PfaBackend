package com.pfa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfa.config.JwtProvider;
import com.pfa.models.User;
import com.pfa.repository.UserRepository;
import com.pfa.request.LoginRequest;
import com.pfa.response.AuthResponse;
import com.pfa.service.CustomerUserDetailsService;
import com.pfa.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerUserDetailsService customerUserDetails;
	
	@PostMapping("/signup")
	public AuthResponse createUser(@RequestBody User user) throws Exception {
		
		User isExist = userRepository.findByEmail(user.getEmail());
		if(isExist!=null) {
			throw new Exception("This email already used with another account");
		}
		
		
		
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Corrected line
		
		User savedUser = userRepository.save(newUser);
		
		Authentication authentication= new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		String token= JwtProvider.generateToken(authentication);
		
		AuthResponse res= new AuthResponse(token,"Register Success");
		
		return res;
	}
	
	@PostMapping("/signin")
	public AuthResponse signin(@RequestBody LoginRequest loginRequest) {
		Authentication authentication=
				authenticate(loginRequest.getEmail(),loginRequest.getPassword());
		
String token= JwtProvider.generateToken(authentication);
		
		AuthResponse res= new AuthResponse(token,"Login Success");
		
		return res;
	}

	private Authentication authenticate(String email, String password) {
		UserDetails userDetails = customerUserDetails.loadUserByUsername(email);
		if(userDetails==null) {
			throw new BadCredentialsException("invalid username");
			
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("password not matched");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,
				null,
				userDetails.getAuthorities());
	}
}

