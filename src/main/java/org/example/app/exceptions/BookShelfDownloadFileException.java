package org.example.app.exceptions;

public class BookShelfDownloadFileException extends Exception {
	
	public final String message;

	public BookShelfDownloadFileException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}	
}
