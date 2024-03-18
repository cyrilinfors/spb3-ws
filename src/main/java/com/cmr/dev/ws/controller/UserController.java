package com.cmr.dev.ws.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmr.dev.business.dto.UserBasicDTO;
 import com.cmr.dev.business.dto.UserFullDTO;
import com.cmr.dev.business.dto.UserRegistrationDTO;
import com.cmr.dev.business.dto.UserRegistrationResponseDTO;
import com.cmr.dev.business.dto.UserRequestDTO;
import com.cmr.dev.business.exception.Spb3BusinessException;
import com.cmr.dev.business.service.IUserService;
import com.cmr.dev.business.utils.ConstsValues;
import com.cmr.dev.ws.jwt.service.JwtService;

import jakarta.annotation.Resource;

/**
 * RESTful controller to manage user-related operations.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Resource(name = ConstsValues.ServiceKeys.USER_SERVICE_KEY)
	private IUserService userService;

	/**
	 * Constructor for UserController.
	 *
	 * @param jwtService            Service for handling JWT operations.
	 * @param authenticationManager Spring Security AuthenticationManager.
	 */
	public UserController(JwtService jwtService, AuthenticationManager authenticationManager) {
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Create a new user.
	 *
	 * @param userDTO Information about the user to create.
	 * @return ResponseEntity containing information about the newly created user.
	 * @throws Spb3BusinessException If a business exception occurs.
	 */
	@PostMapping
	public ResponseEntity<UserFullDTO> createUser(@RequestBody UserFullDTO userDTO) throws Spb3BusinessException {
		UserFullDTO createdUser = userService.create(userDTO);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	/**
	 * Retrieve information about a user by their identifier.
	 *
	 * @param id The identifier of the user.
	 * @return ResponseEntity containing information about the user.
	 * @throws Spb3BusinessException If a business exception occurs.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserFullDTO> getUserById(@PathVariable Integer id) throws Spb3BusinessException {
		UserFullDTO userDTO = userService.findById(id);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}

	/**
	 * Retrieve a list of all users.
	 *
	 * @return ResponseEntity containing the list of users.
	 */
	@GetMapping
	public ResponseEntity<List<UserBasicDTO>> getAllUsers() {
		List<UserBasicDTO> companies = userService.findAll();
		return new ResponseEntity<>(companies, HttpStatus.OK);
	}

	/**
	 * Retrieve information about a user by their email.
	 *
	 * @param email The email of the user.
	 * @return ResponseEntity containing information about the user.
	 * @throws Spb3BusinessException If a business exception occurs.
	 */
	@GetMapping("by-email/{email}")
	public ResponseEntity<UserFullDTO> getUserByEmail(@PathVariable String email) throws Spb3BusinessException {
		UserFullDTO dto = this.userService.findUserByEmail(email);
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	/**
	 * Register a new user.
	 *
	 * @param dto Information about the user to register.
	 * @return ResponseEntity containing information about the registration
	 *         response.
	 * @throws Spb3BusinessException If a business exception occurs.
	 */
	@PostMapping("/register")
	public ResponseEntity<UserRegistrationResponseDTO> registerUser(@RequestBody UserRegistrationDTO dto)
			throws Spb3BusinessException {
		UserRegistrationResponseDTO response = userService.registerUser(dto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Authenticate a user and generate a JWT token.
	 *
	 * @param authRequest Information about the user for authentication.
	 * @return ResponseEntity containing the generated JWT token.
	 * @throws Spb3BusinessException If a business exception occurs.
	 */
	@PostMapping("/generateToken")
	public ResponseEntity<Map<String, String>> authenticateAndGetToken(@RequestBody UserRequestDTO authRequest)
			throws Spb3BusinessException {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			UserFullDTO user = userService.findUserByEmail(authRequest.getEmail());
			List<String> roles = this.userService.loadUserByUsername(authRequest.getEmail()).getAuthorities().stream()
					.map(GrantedAuthority::getAuthority).toList();
			return ResponseEntity.ok(Map.of("token", jwtService.generateToken(user, roles)));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
		}
	}
}
