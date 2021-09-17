package org.example.web.dto;

import org.springframework.stereotype.Repository;

@Repository
public class NameFile {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
