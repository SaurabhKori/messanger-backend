package com.saurabh.messge_backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurabh.messge_backend.dao.UserLoginRegDao;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.UserLogin;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.utils.EncrepytDecrypt;
import com.saurabh.messge_backend.utils.JwtUtil;
import com.saurabh.messge_backend.utils.PasswordUtil;
import com.saurabh.messge_backend.utils.ValidationUtil;

@RestController
@CrossOrigin(origins = { "http://localhost:4200/", "*" })
public class UserRegLoginController {
//	 private final AuthenticationManager authManager;
	@Autowired
	private final JwtUtil jwtUtil; // your JWT helper

//	public UserRegLoginController(AuthenticationManager am, JwtUtil ju) {
//	    this.authManager = am;
//	    this.jwtUtil = ju;
//	}
	public UserRegLoginController(JwtUtil jw) {
		this.jwtUtil = jw;
	}

	@Autowired
	private UserLoginRegDao userLogin;
	@Autowired
	private EncrepytDecrypt encrypt;
	@Autowired
	private ValidationUtil validation;
	@Autowired
	private PasswordUtil paswaordEncoder;

	@PostMapping("/userReg")
	public ResponseEntity<?> userRegistration(@RequestBody Map<String, String> payload) {

		String user = encrypt.decryptAesIV(payload.get("payload"));
		ObjectMapper map = new ObjectMapper();
		try {
			UserLogin users = map.readValue(user, UserLogin.class);
			Map<String, String> errors = validation.validate(users);
			if (!errors.isEmpty()) {
				return ResponseEntity.badRequest().body(errors);
			}
//         users.setUser_password(paswaordEncoder.hashPassword(users.getUser_password()));
			return userLogin.userRegistration(users);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Server Error");
		}

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
		// dto.username should actually be the phone number
//	        Authentication auth = authManager.authenticate(
//	          new UsernamePasswordAuthenticationToken(dto.getUserPhone(), dto.getUser_password())
//	        );
//
//	        String token = jwtUtil.generateToken();
//	        return ResponseEntity.ok(Collections.singletonMap("token", token));
		String value = encrypt.decryptAesIV(payload.get("payload"));
		ObjectMapper mapper = new ObjectMapper();
		UserLogin users;
		try {
			users = mapper.readValue(value, UserLogin.class);
			Optional<UserLogin> user = Optional.ofNullable(userLogin.findByUserPhone(users.getUserPhone()));

			if (user.isPresent() && user.get().getUser_password().equals(users.getUser_password())) {
				String token = jwtUtil.generateToken(user.get().getUserPhone(), user.get().getUserId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user", user.get());
				map.put("token", token);
				String data = encrypt.encryptAesIV(mapper.writeValueAsString(map));
				System.out.print(data);
				System.out.println(encrypt.decryptAesIV(data));
				return new ResponseEntity<>(ApiResponse.builder().status(HttpStatus.OK).message("Success").payload(data)
						.success(true).build(), HttpStatus.ACCEPTED);
			}
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");

	}

	private static StringBuffer UPLOAD_DIR = new StringBuffer("D:/uploads/");

//	@PostMapping("/upoadAttachment")
	public ResponseEntity<ApiResponse> uploadFile(@RequestBody Map<String, String> payload) {
		@SuppressWarnings("static-access")
		String file = encrypt.decryptAesIV(payload.get("payload"));
		System.out.println(file);
		ObjectMapper obj = new ObjectMapper();
		try {
			FileAttachment files = obj.readValue(file, FileAttachment.class);
			if (files.getUserId() != null) {
				System.out.println(files.getUserId());
				UserLogin user = new UserLogin();
				user.setUserId(files.getUserId());

//				files.setUser_id(user);
				UPLOAD_DIR.append("/");
				UPLOAD_DIR.append(files.getDestination());

				Path dirPath = Paths.get(UPLOAD_DIR.toString());

				try {
					if (!Files.exists(dirPath)) {
						Files.createDirectories(dirPath);

					}
					System.out.println("Directory created: " + dirPath.toAbsolutePath());
					byte[] decodedBytes = Base64.getDecoder().decode(files.getBase64());
					String fileName = System.currentTimeMillis() + files.getFile_name();
					UPLOAD_DIR.append("/");
					UPLOAD_DIR.append(fileName);
					files.setFile_name(fileName);
					files.setFileurl(UPLOAD_DIR.toString());
					Path filePath = dirPath.resolve(fileName);
					Files.write(filePath, decodedBytes, StandardOpenOption.CREATE);

					System.out.println("File saved at: " + filePath.toAbsolutePath());
//					user.setAttachments(Arrays.asList(files));
					UserLogin users = userLogin.updateByUserId(user);
					String data = encrypt.encryptAesIV(users.toString());
					return new ResponseEntity<>(ApiResponse.builder().status(HttpStatus.OK).message("Success")
							.payload(data).success(true).build(), HttpStatus.ACCEPTED);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new ResponseEntity<>(ApiResponse.builder().message("Not Success")
							.status(HttpStatus.INTERNAL_SERVER_ERROR).success(false).build(),
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(ApiResponse.builder().message("Not Success")
					.status(HttpStatus.INTERNAL_SERVER_ERROR).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(ApiResponse.builder().message("Not Success")
				.status(HttpStatus.INTERNAL_SERVER_ERROR).success(false).build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
