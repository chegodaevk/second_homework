package org.example.app.services;

import java.util.List;

import org.example.web.dto.Book;
import org.example.web.dto.NameFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	private final ProjectRepository<Book> bookRepo;
	private final DataNameFile dataNameFile;
	
	@Autowired
	public BookService(ProjectRepository<Book> bookRepo, DataNameFile dataNameFile) {
		this.bookRepo = bookRepo;
		this.dataNameFile = dataNameFile;
	}

	public List<Book> getAllBooks() {		
		return bookRepo.retreiveAll();
	}

	public void saveBook(Book book) {
		bookRepo.store(book);		
	}

	public boolean removeBookById(Integer bookIdToRemove) {
		return bookRepo.removeItemById(bookIdToRemove);
	}	
				
	public void deleteDataByAuthor(String authorBook) {
		bookRepo.deleteBooksByAuthor(authorBook);
	}

	public void deleteDataBySize(Integer sizeBook) {
		bookRepo.deleteBooksBySize(sizeBook);
	}

	public void deleteDataByTitle(String titleBook) {
		bookRepo.deleteBookByTitle(titleBook);				
	}
	
	public List<Book> sortingAuthors() {
		return bookRepo.sortAuthors();		
	}

	public List<Book> sortingTitles() {
		return bookRepo.sortTitles();		
	}

	public List<Book> sortingSizes() {
		return bookRepo.sortSizes();		
	}
	
	public void saveNameFile(String name) {
		dataNameFile.addNameFile(name);		
	}
	
	public List<NameFile> getAllFile() {
		return dataNameFile.retreiveAll();
	}	
}
