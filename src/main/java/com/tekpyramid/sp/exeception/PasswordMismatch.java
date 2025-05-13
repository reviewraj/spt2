package com.tekpyramid.sp.exeception;

public class PasswordMismatch extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordMismatch(String message) {
		super(message);
	}

}
