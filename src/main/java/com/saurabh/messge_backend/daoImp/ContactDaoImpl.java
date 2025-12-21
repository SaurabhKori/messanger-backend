package com.saurabh.messge_backend.daoImp;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.saurabh.messge_backend.dao.ContactDao;
import com.saurabh.messge_backend.dao.UserLoginRegDao;
import com.saurabh.messge_backend.dto.ContactUserRecord;
import com.saurabh.messge_backend.model.Contact;
import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.UserLogin;
import com.saurabh.messge_backend.payload.ApiResponse;
import com.saurabh.messge_backend.repo.ContactRepo;
import com.saurabh.messge_backend.repo.FileAttachmentRepo;
import com.saurabh.messge_backend.utils.EncrepytDecrypt;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactDaoImpl implements ContactDao {
	@Autowired
	UserLoginRegDao userDao;
	@Autowired
	private ContactRepo contactRepo;
	@Autowired
	private EncrepytDecrypt encrypt;
	@Autowired
	private FileAttachmentRepo fileAttachmentRepo;

	@Override
	public ResponseEntity<ApiResponse> saveContact(Long userid, Long phone_number) {
		UserLogin contactDetail = userDao.findByUserPhone(Long.toString(phone_number));
		UserLogin user = userDao.findByUserID(userid);
		if (contactRepo.existsByContactUser_UserId(contactDetail.getUserId()))
			return new ResponseEntity<>(ApiResponse.builder().message("Already Exists")
					.status(HttpStatus.NOT_ACCEPTABLE).success(true).build(), HttpStatus.NOT_ACCEPTABLE);

		if (contactDetail != null && user != null) {
			Contact contact = new Contact();
			contact.setUser(user);
			contact.setContactUser(contactDetail);
			Contact contacts = contactRepo.save(contact);
			return new ResponseEntity<>(ApiResponse.builder().message("Contact add Successfuly")
					.status(HttpStatus.ACCEPTED).success(true).build(), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(
				ApiResponse.builder().message("Invalid Contact").status(HttpStatus.BAD_REQUEST).success(false).build(),
				HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<ApiResponse> getContactByUserId(Long userid) {
		try {
			if (userid == null)
				return new ResponseEntity<>(ApiResponse.builder().message("Bad Request").status(HttpStatus.BAD_REQUEST)
						.success(false).build(), HttpStatus.BAD_REQUEST);
			List<Contact> contacts = contactRepo.findByUser_UserId(userid);
			List<Long> contactId = contacts.stream().map(contact -> contact.getContactUser().getUserId()).toList();

//		    List<FileAttachment> fileAttachments=fileAttachmentRepo.findByUserid_UserIdInAndDestination(contactId,"profile");
			Map<Long, FileAttachment> profileMap = fileAttachmentRepo
					.findByUserid_UserIdInAndDestination(contactId, "profile").stream()
					.collect(Collectors.toMap(fa -> fa.getUserid().getUserId(), fa -> fa, (a, b) -> a // keep first if
																										// multiple
					));
			log.info("data------->" + contacts);
			List<ContactUserRecord> result = contacts.stream().map(c -> new ContactUserRecord(c.getContactId(),
					c.getContactUser(), profileMap.get(c.getContactUser().getUserId()))).toList();
//			List<ContactUserRecord> result = contactRepo.findContactsWithProfile(userid);
			log.info("data------->" + result);

			ObjectMapper objMapper = new ObjectMapper();
			objMapper.registerModule(new JavaTimeModule());
			String data = encrypt.encryptAesIV(objMapper.writeValueAsString(result));
			return new ResponseEntity<>(ApiResponse.builder().message("Success").status(HttpStatus.ACCEPTED)
					.success(true).payload(data).build(), HttpStatus.ACCEPTED);

		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(
					ApiResponse.builder().message("Bad Request").status(HttpStatus.BAD_REQUEST).success(false).build(),
					HttpStatus.BAD_REQUEST);
		}

	}

}
