package com.aerse.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ValidationResult extends HashMap<String, String> {
	private static final long serialVersionUID = 8108864318907435382L;
	public static final ValidationResult INTERNAL_SERVER_ERROR = new ValidationResult("api.error.internal");
	private UUID requestId;
	private Object response;

	public ValidationResult() {
		// do nothing
	}

	public ValidationResult(String name, String value) {
		put(name, value);
	}

	public ValidationResult(String general) {
		setGeneralError(general);
	}

	public void setGeneralError(String general) {
		put("general", general);
	}

	public String getGeneralError() {
		return get("general");
	}

	public UUID getRequestId() {
		return requestId;
	}

	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}
	
	public Object getResponse() {
		return response;
	}
	
	public void setResponse(Object response) {
		this.response = response;
	}

	public String getFirst() {
		for (String cur : values()) {
			return cur;
		}
		return null;
	}

	public void putAll(String prefix, ValidationResult errors) {
		if (errors != null) {
			for (Map.Entry<String, String> cur : errors.entrySet()) {
				put(prefix + cur.getKey(), cur.getValue());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static <T> ValidationResult valueOf(Set<ConstraintViolation<T>> violations) {
		if (violations == null || violations.isEmpty()) {
			return null;
		}
		ValidationResult result = new ValidationResult();
		for (ConstraintViolation cur : violations) {
			result.put(cur.getPropertyPath().toString(), cur.getMessage());
		}
		return result;
	}

	public static ValidationResult valueOfMethodValidation(Set<? extends ConstraintViolation<?>> violations) {
		if (violations == null || violations.isEmpty()) {
			return null;
		}
		ValidationResult result = new ValidationResult();
		for (ConstraintViolation<?> cur : violations) {
			String pathStr = cur.getPropertyPath().toString();
			int index = pathStr.indexOf(").");
			if (index != -1) {
				pathStr = pathStr.substring(index + 2);
			}
			result.put(pathStr, cur.getMessage());
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static <T> ValidationResult valueOfFirst(Set<ConstraintViolation<T>> violations) {
		if (violations == null || violations.isEmpty()) {
			return null;
		}
		ValidationResult result = new ValidationResult();
		for (ConstraintViolation cur : violations) {
			result.setGeneralError(cur.getMessage());
			return result;
		}
		return null;
	}

	public static ValidationResult valueOf(BindingResult validation) {
		ValidationResult result = new ValidationResult();
		for (FieldError cur : validation.getFieldErrors()) {
			result.put(cur.getField(), fixErrorMessage(cur.getDefaultMessage()));
		}
		return result;
	}

	private static String fixErrorMessage(String message) {
		if (message.contains("java.lang.NumberFormatException")) {
			if (Locale.getDefault().getLanguage().equalsIgnoreCase("ru")) {
				return "Неверный формат";
			} else {
				return "Invalid format";
			}
		}
		return message;
	}

	public static <T> ValidationResult valueOf(String prefix, Set<ConstraintViolation<T>> constraints) {
		if (constraints.isEmpty()) {
			return new ValidationResult();
		}
		ValidationResult result = new ValidationResult();
		for (ConstraintViolation<?> cur : constraints) {
			result.put(prefix + "." + cur.getPropertyPath().toString(), cur.getMessage());
		}
		return result;
	}

}
