package com.saurabh.messge_backend.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Table(name = "file_attachment")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileAttachment {
	public UserLogin getUserid() {
		return userid;
	}

	public void setUserid(UserLogin userid) {
		this.userid = userid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "file_id")
	Long file_id;
	@Column(name = "file_name", nullable = false)
	String file_name;
	@Column(name = "fileurl", nullable = false)
	String fileurl;
	@Column(name = "file_type", nullable = false)
	String file_type;
	@Column(name = "destination")
	String destination;
	LocalDateTime uploadtime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	UserLogin userid;
	@Transient
	@JsonProperty
	String base64;
	@Transient
	@JsonProperty
	Long userId;

	public FileAttachment(Long file_id, String file_name, String fileurl, String file_type, String destination,
			String base64, Long userId) {

		this.file_id = file_id;
		this.file_name = file_name;
		this.fileurl = fileurl;
		this.file_type = file_type;
		this.destination = destination;

		this.base64 = base64;
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUser_id(UserLogin user) {
		this.userid = user;
	}

	public UserLogin getUser_id() {
		return userid;
	}
}
