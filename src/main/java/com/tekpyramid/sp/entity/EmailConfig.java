package com.tekpyramid.sp.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tekpyramid.sp.utility.AuditableDocument;

@Document
public class EmailConfig extends AuditableDocument{
	@Id
	private String emailConfigId;
	private String action;
	private String description;
	private boolean enabled;
	public String getEmailConfigId() {
		return emailConfigId;
	}
	public void setEmailConfigId(String emailConfigId) {
		this.emailConfigId = emailConfigId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
