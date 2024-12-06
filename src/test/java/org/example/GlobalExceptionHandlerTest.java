package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setup() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleNotFound() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/nonexistent", HttpHeaders.EMPTY);
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleNotFound(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.NOT_FOUND.value(), model.getAttribute("status"));
        assertEquals("Page Not Found", model.getAttribute("error"));
        assertEquals("The page you are looking for does not exist.", model.getAttribute("message"));
        assertEquals("/nonexistent", model.getAttribute("path"));
    }

    @Test
    public void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Test resource not found");
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleResourceNotFound(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.NOT_FOUND.value(), model.getAttribute("status"));
        assertEquals("Resource Not Found", model.getAttribute("error"));
        assertEquals("Test resource not found", model.getAttribute("message"));
    }

    @Test
    public void testHandleAllExceptions() {
        Exception ex = new Exception("Generic error");
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleAllExceptions(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), model.getAttribute("status"));
        assertEquals("Internal Server Error", model.getAttribute("error"));
        assertEquals("An unexpected error occurred.", model.getAttribute("message"));
    }

    @Test
    public void testHandleInvalidInput() {
        InvalidInputException ex = new InvalidInputException("Invalid input provided");
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleInvalidInput(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.BAD_REQUEST.value(), model.getAttribute("status"));
        assertEquals("Invalid Input", model.getAttribute("error"));
        assertEquals("Invalid input provided", model.getAttribute("message"));
    }

    @Test
    public void testHandleDuplicateResource() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate resource found");
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleDuplicateResource(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.CONFLICT.value(), model.getAttribute("status"));
        assertEquals("Duplicate Resource", model.getAttribute("error"));
        assertEquals("Duplicate resource found", model.getAttribute("message"));
    }

    @Test
    public void testHandleFeatureDisabled() {
        FeatureDisabledException ex = new FeatureDisabledException("Feature disabled at this moment");
        Model model = new ConcurrentModel();

        String viewName = exceptionHandler.handleFeatureDisabled(ex, model);

        assertEquals("error", viewName);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), model.getAttribute("status"));
        assertEquals("Feature Disabled", model.getAttribute("error"));
        assertEquals("Feature disabled at this moment", model.getAttribute("message"));
    }
}
