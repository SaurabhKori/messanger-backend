package com.saurabh.messge_backend.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Table(name = "user_login_reg")
public class UserLogin {
	@Column(name = "user_id")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@NotBlank(message = "User name is required")
//    @Pattern(regexp = "[a-zA-Z]/s{3,20}$", message = "User name must be alphanumeric and 3-20 chars")
	@Column(name = "user_name", unique = true, nullable = false)
	private String user_name;
	@Column(name = "user_password")
	@NotBlank(message = "Password is required")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$", message = "Password must have uppercase, lowercase, number, special char, min 8 chars")
	private String user_password;

	@Column(name = "user_phone", unique = true, nullable = false)
	@NotNull(message = "User number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "User number must be exactly 10 digits")
	private String userPhone; // <-- Changed to String
//	@OneToOne(cascade = CascadeType.ALL)
//	@OneToMany(mappedBy = "userid", cascade = CascadeType.ALL, orphanRemoval = true)
//
//
//	private List<FileAttachment> attachments;

}
