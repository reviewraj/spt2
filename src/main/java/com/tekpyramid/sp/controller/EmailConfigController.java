package com.tekpyramid.sp.controller;

import com.tekpyramid.sp.entity.EmailConfig;
import com.tekpyramid.sp.responseDto.ResponseDto;
import com.tekpyramid.sp.service.EmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/supportDesk/v1/admin/emailConfig")
@Tag(name = "EmailConfig Controller", description = "APIs related to Email Configurations")
public class EmailConfigController {

	@Autowired
	private EmailConfigService emailConfigService;

	@PostMapping("/save")
	public ResponseEntity<ResponseDto> saveEmailConfig(@RequestBody EmailConfig emailConfig) {
		EmailConfig savedEmailConfig = emailConfigService.saveEmailConfig(emailConfig);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDto(false, "EmailConfig saved successfully", savedEmailConfig));
	}

	@GetMapping("/getAll")
	public ResponseEntity<ResponseDto> getAllEmailConfigs() {
		return ResponseEntity.ok()
				.body(new ResponseDto(false, "Fetched all EmailConfigs", emailConfigService.getAllEmailConfigs()));
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<ResponseDto> getEmailConfigById(@PathVariable String id) {
		return emailConfigService.getEmailConfigById(id)
				.map(emailConfig -> ResponseEntity.ok(new ResponseDto(false, "Fetched EmailConfig", emailConfig)))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseDto(true, "EmailConfig not found", null)));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<ResponseDto> updateEmailConfig(@PathVariable String id,
			@RequestBody EmailConfig emailConfig) {
		try {
			EmailConfig updatedEmailConfig = emailConfigService.updateEmailConfig(id, emailConfig);
			return ResponseEntity.ok(new ResponseDto(false, "EmailConfig updated successfully", updatedEmailConfig));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(true, e.getMessage(), null));
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseDto> deleteEmailConfig(@PathVariable String id) {
		emailConfigService.deleteEmailConfig(id);
		return ResponseEntity.ok(new ResponseDto(false, "EmailConfig deleted successfully", null));
	}
}
