package org.example.app.exceptions;

public class BookShelfUploadFileException extends Exception {
	
	public final String message;
		
	public BookShelfUploadFileException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
