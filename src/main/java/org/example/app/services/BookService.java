package org.example.app.services;

import java.util.List;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
	
	private final ProjectRepository<Book> bookRepo;
	private final Logger logger = Logger.getLogger(BookService.class);
	
	@Autowired
	public BookService(ProjectRepository<Book> bookRepo) {
		this.bookRepo = bookRepo;
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
	
	private void defaultInit() {
		logger.info("default INIT in book service");
	}
	
	private void defaultDestroy() {
		logger.info("default DESTROY in book service");
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
}
