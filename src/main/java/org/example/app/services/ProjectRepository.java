package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
	List<T> retreiveAll();
	
	void store(T book);

	boolean removeItemById(Integer bookIdToRemove);

	void deleteBooksByAuthor(String dataOfBook);

	void deleteBooksBySize(Integer sizeBook);

	void deleteBookByTitle(String titleBook);

	List<T> sortSizes();

	List<T> sortTitles();

	List<T> sortAuthors();
}