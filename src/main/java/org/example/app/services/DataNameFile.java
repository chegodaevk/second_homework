package org.example.app.services;

import java.util.ArrayList;
import java.util.List;

import org.example.web.dto.NameFile;
import org.springframework.stereotype.Repository;

@Repository
public class DataNameFile {
	
	private List<NameFile> dataFile = new ArrayList<>();
	
	public void addNameFile(String name) {
		NameFile nameFile = new NameFile();
		nameFile.setName(name);
		dataFile.add(nameFile);		
	}		
	
	public List<NameFile> retreiveAll() {
		return new ArrayList<>(dataFile);
	}
	
	
}
