package com.leothenardo.ecommerce.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AllowedFileExtensionsValidator.class)
@Documented
public @interface AllowedFileExtensions {

	String message() default "{AllowedFileExtensionsValidator.message}";

	String[] value() default {};

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}