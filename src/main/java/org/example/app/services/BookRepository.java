package org.example.app.services;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {
	
	private final Logger logger = Logger.getLogger(BookRepository.class);
	private ApplicationContext context;
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Book> retreiveAll() {
			List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
			Book book = new Book();
			book.setId(rs.getInt("id"));
			book.setAuthor(rs.getString("author"));
			book.setTitle(rs.getString("title"));
			book.setSize(rs.getInt("size"));
			return book;
		});
		return new ArrayList<>(books) ;
	}
	
	// сохранение данных о книги
	@Override
	public void store(Book book) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("author", book.getAuthor());
		parameterSource.addValue("title", book.getTitle());
		parameterSource.addValue("size", book.getSize());
		jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);
		logger.info("store new book: " + book);				
	}
	
	@Override
	public boolean removeItemById(Integer bookIdToRemove) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("id", bookIdToRemove);
		jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
		logger.info("remove book by id completed");	
		return true;
	}
	
	@Override
	public void deleteBooksByAuthor(String authorBook) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("author", authorBook);
		jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
		logger.info("remove book by author completed");		
	}
	
	@Override
	public void deleteBookByTitle(String titleBook) {
		MapSqlParameterSource parametrSource = new MapSqlParameterSource();
		parametrSource.addValue("title", titleBook);
		jdbcTemplate.update("DELETE FROM books WHERE title = :title", parametrSource);
		logger.info("remove book by title completed");
	}
	
	@Override
	public void deleteBooksBySize(Integer sizeBook) {
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("size", sizeBook);
		jdbcTemplate.update("DELETE FROM books WHERE size = :size", parameterSource);
		logger.info("remove book by size completed");
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
	
	private void defaultInit() {
		logger.info("default INIT in book repo bean");
	}
	private void defaultDestroy() {
		logger.info("default DESTROY in book repo bean");
	}
		
	@Override
	public List<Book> sortAuthors() {
		List<Book> sortBooks =retreiveAll();
		for(int k=0; k < sortBooks.size(); k++){
            int mindex = k;
            for(int j=k+1; j < sortBooks.size(); j++){
                if (sortBooks.get(j).getAuthor().compareTo(sortBooks.get(mindex).getAuthor()) < 0) {
                    mindex = j;
                }
            }
            Collections.swap(sortBooks, k, mindex);
        }
		return sortBooks;
	}

	@Override
	public List<Book> sortTitles() {
		List<Book> sortBooks =retreiveAll();
		for(int k=0; k < sortBooks.size(); k++){
            int mindex = k;
            for(int j=k+1; j < sortBooks.size(); j++){
                if (sortBooks.get(j).getTitle().compareTo(sortBooks.get(mindex).getTitle()) < 0) {
                    mindex = j;
                }
            }
            Collections.swap(sortBooks, k, mindex);
        }
		return sortBooks;
	}

	@Override
	public List<Book> sortSizes() {
		List<Book> sortBooks =retreiveAll();
		for(int k=0; k < sortBooks.size(); k++){
			int mindex = k;
			for(int j=k+1; j < sortBooks.size(); j++){
				if (sortBooks.get(j).getSize() < sortBooks.get(mindex).getSize()) {
					mindex = j;
				}
			}
        Collections.swap(sortBooks, k, mindex);
		}		
		return sortBooks;
	}
}
