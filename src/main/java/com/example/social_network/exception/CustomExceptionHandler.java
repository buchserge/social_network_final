package com.example.social_network.exception;

import com.example.social_network.domain.entity.User;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Error error = new Error("errorrrrrr", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String imageTooBig(@AuthenticationPrincipal User user, Model model, Exception ex) {
        ex.printStackTrace();
        System.out.println("----IMAGE IS TOO BIG----");
        model.addAttribute("error", "fileSizeLimit");
        model.addAttribute("friendsUser", user);
        return "errors/noData";
    }

    @ExceptionHandler(UsersNotFoundException.class)
    public String noOne(@AuthenticationPrincipal User user, Model model, UsersNotFoundException ex) {
        ex.printStackTrace();
        System.out.println(ex.getErrMessage());
        model.addAttribute("friendsUser", user);
        model.addAttribute("error", "noSuchElement");
        return "errors/noData";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String duplicateCredentials(RedirectAttributes attributes, Model model, Exception ex) {
        if (ex.getMessage().equals("same credentials")) {
            System.out.println("----SAME credentials----");
            attributes.addFlashAttribute("wrong", "please, provide new credentials");
            ex.printStackTrace();
            return "redirect:showChangeSettings";
        } else {
            ex.printStackTrace();
            System.out.println("----Duplicate credentials----");
            attributes.addFlashAttribute("wrong", "user already exists");
            return "redirect:reg";
        }
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public String badEmail(RedirectAttributes attributes, Model model, Exception ex) {
        ex.printStackTrace();
        System.out.println("----INCORRECT EMAIL----");
        attributes.addFlashAttribute("badEmail", "not valid email provided");
        return "redirect:reg";
    }
}
