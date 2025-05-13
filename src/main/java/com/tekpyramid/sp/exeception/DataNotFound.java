package com.tekpyramid.sp.exeception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DataNotFound extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataNotFound(String message) {
		super(message);
	}
}
