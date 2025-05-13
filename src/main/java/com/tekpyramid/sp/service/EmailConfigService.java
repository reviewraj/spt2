package com.tekpyramid.sp.service;

	import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tekpyramid.sp.entity.EmailConfig;
import com.tekpyramid.sp.repository.EmailConfigRepository;

import lombok.AllArgsConstructor;

	@Service
	@AllArgsConstructor
	public class EmailConfigService {
		
	    
	    private final EmailConfigRepository emailConfigRepository;

	    public EmailConfig saveEmailConfig(EmailConfig emailConfig) {
	        return emailConfigRepository.save(emailConfig);
	    }

	    public List<EmailConfig> getAllEmailConfigs() {
	        return emailConfigRepository.findAll();
	    }

	    public Optional<EmailConfig> getEmailConfigById(String id) {
	        return emailConfigRepository.findById(id);
	    }

	    public EmailConfig updateEmailConfig(String id, EmailConfig emailConfig) {
	        if (emailConfigRepository.existsById(id)) {
	            emailConfig.setEmailConfigId(id);
	            return emailConfigRepository.save(emailConfig);
	        } else {
	            throw new RuntimeException("EmailConfig not found");
	        }
	    }

	    public void deleteEmailConfig(String id) {
	        emailConfigRepository.deleteById(id);
	    }
	}


