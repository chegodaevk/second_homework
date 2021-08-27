package org.example.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// для обработки всех исключений в одном классе используется аннотация ControllerAdvice
//@ControllerAdvice
@Controller
public class ErrorController {
		
	@GetMapping("/404")
	public String notFoundError() {
		return "errors/404";
	}		
}
