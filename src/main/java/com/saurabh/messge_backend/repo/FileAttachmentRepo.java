package com.saurabh.messge_backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.UserLogin;

public interface FileAttachmentRepo extends JpaRepository<FileAttachment, Integer> {
	public List<FileAttachment> findByUseridAndDestination(UserLogin userid, String destination);

	public List<FileAttachment> findByUserid_UserIdAndDestination(Long userid, String destination);

	public List<FileAttachment> findByUserid_UserIdInAndDestination(List<Long> userid, String destination);

	void deleteByUserid_UserIdAndDestination(Long userid, String destination);
}
