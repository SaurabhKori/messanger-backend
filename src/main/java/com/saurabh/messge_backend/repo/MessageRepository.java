package com.saurabh.messge_backend.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.saurabh.messge_backend.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	// Private chat history
	@Query("""
			    SELECT m FROM Message m
			    WHERE (m.sender.userId = :u1 AND m.receiver.userId = :u2)
			       OR (m.sender.userId = :u2 AND m.receiver.userId = :u1)
			    ORDER BY m.createdAt
			""")
	List<Message> getPrivateChat(@Param("u1") Long user1, @Param("u2") Long user2);

	// Group chat history
	List<Message> findByGroup_GroupIdOrderByCreatedAt(Long groupId);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("""
			    UPDATE Message m
			    SET m.deliveredAt = :time
			    WHERE m.messageId = :id
			      AND m.deliveredAt IS NULL
			""")
	int markDelivered(@Param("id") Long id, @Param("time") LocalDateTime time);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("""
			    UPDATE Message m
			    SET m.seenAt = :time
			    WHERE m.messageId = :id
			      AND m.seenAt IS NULL
			""")
	int markSeen(@Param("id") Long id, @Param("time") LocalDateTime time);

	@Modifying
	@Transactional
	@Query("""
			   UPDATE Message m
			   SET m.deliveredAt = :time
			   WHERE m.receiver.userId = :receiverId
			     AND m.sender.userId = :senderId
			     AND m.deliveredAt IS NULL
			""")
	int markHistoryDelivered(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId,
			@Param("time") LocalDateTime time);

}
