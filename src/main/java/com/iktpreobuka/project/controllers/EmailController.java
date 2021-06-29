package com.iktpreobuka.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.project.entities.EmailObject;
import com.iktpreobuka.project.services.EmailService;

@RestController
@RequestMapping(path = "/")
public class EmailController {

	
	@Autowired
	private EmailService emailService;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/simpleEmail")
	public String sendSimpleMessage(@RequestBody EmailObject object) {
		
		if (object == null || object.getTo() == null || object.getText() == null)
			return null;
		
		emailService.sendSimpleMessage(object);
		
		return "Your simple email has been sent!";
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/templateEmail")
	public String sendTemplateMessage(@RequestBody EmailObject object) throws Exception {
		
		if (object == null || object.getTo() == null || object.getText() == null)
			return null;
		
		emailService.sendTemplateMessage(object);
		
		return "Your template email has been sent!";
	}
	
	
	// If you using Postman for testing, add this part to the Headers: Content-Type: application/json
	@RequestMapping(method = RequestMethod.POST, value = "/emailWithAttachment")
	public String sendMessageWithAttachment(@RequestParam String path, @RequestBody EmailObject object) throws Exception {
		
		if (object == null || object.getTo() == null || object.getText() == null)
			return null;
		
		emailService.sendMessageWithAttachment(object, path);
		
		return "Your email with attachment has been sent!";
	}
}
