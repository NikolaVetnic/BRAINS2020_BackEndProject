package com.iktpreobuka.project.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FileHandler {

	public String singleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes);
}
