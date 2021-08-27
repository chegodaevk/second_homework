package org.example.web.dto;

import javax.validation.constraints.NotBlank;

public class BookTitleToRemove {
	
	@NotBlank(message = "the title field must not be empty")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}	
}
