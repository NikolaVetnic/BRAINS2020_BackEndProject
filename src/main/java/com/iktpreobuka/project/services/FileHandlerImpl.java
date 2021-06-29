package com.iktpreobuka.project.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.iktpreobuka.project.entities.UserEntity;
import com.iktpreobuka.project.repositories.UserRepository;

@Service
public class FileHandlerImpl implements FileHandler {
	
	
	@Autowired
	private UserRepository userRepository;

	
	@Override
	public String singleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) {
		
		// da li je fajl prazan
		if (file.isEmpty()) {
			
			// ako jeste, javiti korisniku
			redirectAttributes.addFlashAttribute("message", "No file selected! Select a file!");
			return "redirect:fileTransferStatus";	
		}
		
		try {
			
			// ako nije, odradimo cuvanje na disku
			byte[] bytes = file.getBytes();
			Path path = Paths.get("/Users/nikolavetnic/Desktop/SQL/" + file.getOriginalFilename());
			Files.write(path, bytes);
			redirectAttributes.addFlashAttribute("message", "File " + file.getOriginalFilename() + " successfully uploaded!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// javimo korisniku da je sve ok
		return "redirect:/fileTransferStatus";
	}
}
