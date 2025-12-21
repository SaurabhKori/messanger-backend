package com.saurabh.messge_backend.dto;

import com.saurabh.messge_backend.model.FileAttachment;
import com.saurabh.messge_backend.model.UserLogin;

public record ContactUserRecord(Long contactId, UserLogin contactUser, FileAttachment file) {

}
