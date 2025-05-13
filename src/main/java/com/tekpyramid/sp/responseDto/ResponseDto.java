package com.tekpyramid.sp.responseDto;


import lombok.Data;

@Data
public class ResponseDto {
	
	private boolean error;
	private String message;
	private Object data;
	public ResponseDto(boolean error, String message, Object data) {
		super();
		this.error = error;
		this.message = message;
		this.data = data;
	}
	public ResponseDto() {
		super();
	}
	

}
