package com.saurabh.messge_backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saurabh.messge_backend.dto.ContactUserRecord;
import com.saurabh.messge_backend.model.Contact;

public interface ContactRepo extends JpaRepository<Contact, Long> {
	List<Contact> findByUser_UserId(Long userid);

	boolean existsByContactUser_UserId(Long userId);

	@Query("""
			SELECT new com.saurabh.messge_backend.dto.ContactUserRecord(
			    c.contactId,
			    c.contactUser,
			    f
			)
			FROM Contact c
			LEFT JOIN FileAttachment f
			    ON f.userid.userId = c.contactUser.userId
			   AND f.destination = 'profile'
			WHERE c.user.userId = :userId
			""")
	List<ContactUserRecord> findContactsWithProfile(@Param("userId") Long userId);
}
