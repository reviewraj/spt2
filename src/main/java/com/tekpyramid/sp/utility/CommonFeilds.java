package com.tekpyramid.sp.utility;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
@Data
@MappedSuperclass
public class CommonFeilds {
	private LocalDateTime createOn=LocalDateTime.now();
	private String CreatedByName;
	private String CreatedByemail;
	private String ModifiedByName;
	private String ModifiedByemail;
	private String ModifiedOn;
	
}
