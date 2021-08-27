package org.example.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.example.app.exceptions.BookShelfDownloadFileException;
import org.example.app.exceptions.BookShelfUploadFileException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookAuthorToRemove;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookSizeToRemove;
import org.example.web.dto.BookTitleToRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/books")
@Scope("singleton")
public class BookShelfController {
	
	private Logger logger = Logger.getLogger(BookShelfController.class);
	private BookService bookService;	
	
	@Autowired
	public BookShelfController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping("/shelf")
	public String books(Model model) {
		logger.info(this.toString());
		model.addAttribute("book", new Book());
		model.addAttribute("bookIdToRemove", new BookIdToRemove());
		model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
		model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
		model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
		model.addAttribute("bookList", bookService.getAllBooks());
		return "book_shelf";
	}
	
	@PostMapping("/save")
	public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("book", book);
			model.addAttribute("bookIdToRemove", new BookIdToRemove());
			model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
			model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
			model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
			model.addAttribute("bookList", bookService.getAllBooks());
			return "book_shelf";			
		} else {
			bookService.saveBook(book);
			logger.info("current repository size: " + bookService.getAllBooks().size());
			return "redirect:/books/shelf";
		}		
	}

	@PostMapping("/remove") 
	public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("book", new Book());
			model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
			model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
			model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
			model.addAttribute("bookList", bookService.getAllBooks());
			return "book_shelf";
		} else {
			bookService.removeBookById(bookIdToRemove.getId());
			return "redirect:/books/shelf";
		}		
	}	
	
	@PostMapping("/deleteAuthor")
	public String deleteBookByAuthor(@Valid BookAuthorToRemove bookAuthorToRemove, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("book", new Book());
			model.addAttribute("bookIdToRemove", new BookIdToRemove());
			model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
			model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
			model.addAttribute("bookList", bookService.getAllBooks());
			return "book_shelf";
		} else {
			bookService.deleteDataByAuthor(bookAuthorToRemove.getAuthor());
			return "redirect:/books/shelf";
		}		
	}
	
	@PostMapping("/deleteTitle")
	public String deleteBookByTitle(@Valid BookTitleToRemove bookTitleToRemove, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("book", new Book());
			model.addAttribute("bookIdToRemove", new BookIdToRemove());
			model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
			model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
			model.addAttribute("bookList", bookService.getAllBooks());
			return "book_shelf";			
		} else {
			bookService.deleteDataByTitle(bookTitleToRemove.getTitle());
			return "redirect:/books/shelf";
		}
	}	

	@PostMapping("/deleteSize")
	public String deleteBookBySize(@Valid BookSizeToRemove bookSizeToRemove, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("book", new Book());
			model.addAttribute("bookIdToRemove", new BookIdToRemove());
			model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
			model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
			model.addAttribute("bookList", bookService.getAllBooks());
			return "book_shelf";			
		} else {
			bookService.deleteDataBySize(bookSizeToRemove.getSize());
			return "redirect:/books/shelf";
		}
	}
	
	// загрузка файла
	@PostMapping("/uploadFile")
	public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception, BookShelfUploadFileException {
		if(!file.isEmpty()) {
			String name = file.getOriginalFilename();
			byte[] bytes = file.getBytes();
			
			String rootPath = System.getProperty("catalina.home");
			File dir = new File(rootPath + File.separator + "external_uploads");
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			
			logger.info("new file saved at: " + serverFile.getAbsolutePath());
			
			return "redirect:/books/shelf";
		} else {
			throw new BookShelfUploadFileException("do not specify the file to upload");
		}			
	}
	
	@ExceptionHandler(BookShelfUploadFileException.class)
	public String handleError(Model model, BookShelfUploadFileException exception) {
		model.addAttribute("errorMessage", exception.getMessage());
		return "errors/500";		
	}
	
	@PostMapping("/download")
	public String downloadFile(@RequestParam("file") MultipartFile file) throws Exception, BookShelfDownloadFileException {
		if(!file.isEmpty()) {
			String name = file.getOriginalFilename();
			byte[] bytes = file.getBytes();			
			String rootPath = System.getProperty("user.home");
			File dir = new File(rootPath + File.separator + "Downloads");
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();
			
			logger.info("new file saved at: " + serverFile.getAbsolutePath());
			
			return "redirect:/books/shelf";
		} else {
			throw new BookShelfUploadFileException("do not specify the file to download");
		}	
	}
	
	@ExceptionHandler(BookShelfDownloadFileException.class)
	public String handleError(Model model, BookShelfDownloadFileException exception) {
		model.addAttribute("errorMessage", exception.getMessage());
		return "errors/500";		
	}
	
	@PostMapping("/sortingByAuthors")
	public String sortingByAuthors(Model model) {		 		
		model.addAttribute("book", new Book());
		model.addAttribute("bookIdToRemove", new BookIdToRemove());
		model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
		model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
		model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
		model.addAttribute("bookList", bookService.sortingAuthors());
		return "book_shelf";			
	}
	
	@PostMapping("/sortingByTitle")
	public String sortingByTitle(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("bookIdToRemove", new BookIdToRemove());
		model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
		model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
		model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
		model.addAttribute("bookList", bookService.sortingTitles());
		return "book_shelf";			
	}
	
	@PostMapping("/sortingBySize")
	public String sortingBySize(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("bookIdToRemove", new BookIdToRemove());
		model.addAttribute("bookAuthorToRemove", new BookAuthorToRemove());
		model.addAttribute("bookTitleToRemove", new BookTitleToRemove());
		model.addAttribute("bookSizeToRemove", new BookSizeToRemove());
		model.addAttribute("bookList", bookService.sortingSizes());
		return "book_shelf";			
	}
}
