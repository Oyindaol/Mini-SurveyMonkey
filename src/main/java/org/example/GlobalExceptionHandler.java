package org.example;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The page you are looking for does not exist.");
        model.addAttribute("path", ex.getRequestURL());
        return "error";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", "Resource Not Found");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "An unexpected error occurred.");
        return "error";
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(InvalidInputException ex, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", "Invalid Input");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateResource(DuplicateResourceException ex, Model model) {
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("error", "Duplicate Resource");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(FeatureDisabledException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleFeatureDisabled(FeatureDisabledException ex, Model model) {
        model.addAttribute("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        model.addAttribute("error", "Feature Disabled");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
}
