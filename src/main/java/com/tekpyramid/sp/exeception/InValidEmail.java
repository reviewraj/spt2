package com.tekpyramid.sp.exeception;

public class InValidEmail extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InValidEmail(String message) {
		super(message);
	}

}
