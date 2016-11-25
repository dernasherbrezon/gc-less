package com.aerse.validation;

public class ValidationException extends Exception {

	private static final long serialVersionUID = -32310723499240333L;
	private final ValidationResult validation;
	
	public ValidationException(ValidationResult validation) {
		this.validation = validation;
	}
	
	public ValidationException(String generalError) {
		super(generalError);
		validation = new ValidationResult(generalError);
	}
	
	public ValidationResult getValidation() {
		return validation;
	}
	
}
