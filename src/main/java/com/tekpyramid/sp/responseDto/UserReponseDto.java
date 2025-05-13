package com.tekpyramid.sp.responseDto;

import java.util.Date;

import com.tekpyramid.sp.enums.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserReponseDto {
	private String userid;
	private String UserName;
	private String email;
	private String PhoneNo;
	private String privilege;
	private String role;
	@Enumerated(EnumType.STRING)
	private Status status ;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String modifiedBy;
    private Long noOfTickets;
	public UserReponseDto(String userName, String email) {
		super();
		UserName = userName;
		this.email = email;
	}
	public UserReponseDto() {
		super();
	}



}
