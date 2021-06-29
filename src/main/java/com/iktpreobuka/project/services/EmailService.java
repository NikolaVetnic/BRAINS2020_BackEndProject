package com.iktpreobuka.project.services;

import com.iktpreobuka.project.entities.EmailObject;

public interface EmailService {

	void sendSimpleMessage(EmailObject object);
	void sendTemplateMessage(EmailObject object) throws Exception;
	void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception;
}
