package org.example.app.exceptions;

public class BookShelfLoginException extends Exception {
	
	public final String message;

	public BookShelfLoginException(String message) {
		this.message = message;
	} 
	
	@Override
	public String getMessage() {
		return message;
	}
}