package com.tekpyramid.sp.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
@Documented
@Constraint(validatedBy = PrivilegeNameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrivilegeName {
	 String message() default "Invalid Privilege name";
	    Class<?>[] groups() default {};
	    Class<? extends Payload>[] payload() default {};
}
