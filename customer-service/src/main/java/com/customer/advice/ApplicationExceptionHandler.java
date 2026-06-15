package com.customer.advice;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.customer.exceptions.CustomerNotFoundException;
import com.customer.exceptions.InsufficientBalanceException;
import com.customer.exceptions.InsufficientShareException;

@ControllerAdvice
public class ApplicationExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ProblemDetail handleException(CustomerNotFoundException exception) {
		return build(HttpStatus.NOT_FOUND, exception, problem -> {
			problem.setType(URI.create("https://www.example.com/problems/customer-not-found"));
			problem.setTitle("Customer Not Found !");
		});
		
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ProblemDetail handleException(InsufficientBalanceException exception) {
		return build(HttpStatus.BAD_REQUEST, exception, problem -> {
			problem.setType(URI.create("https://www.example.com/problems/insufficient-balance"));
			problem.setTitle("Insufficient Balance !");
		});
		
	}
	
	@ExceptionHandler(InsufficientShareException.class)
	public ProblemDetail handleException(InsufficientShareException exception) {
		return build(HttpStatus.BAD_REQUEST, exception, problem -> {
			problem.setType(URI.create("https://www.example.com/problems/insufficient-shares"));
			problem.setTitle("Insufficient Shares !");
		});
		
	}
	
	private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
		consumer.accept(problem);
		return problem;
		
	}
}
