package com.tekpyramid.sp.responseDto;

import java.util.Date;

import lombok.Data;
@Data
public class PrivilegeResponseDto {
	private String privilegeId;
	private String privilege;
	private Long numberOfUsers;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String modifiedBy;
}
