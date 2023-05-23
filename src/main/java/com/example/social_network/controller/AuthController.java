package com.example.social_network.controller;

import com.example.social_network.domain.ImageInfo;
import com.example.social_network.domain.dto.UserDTO;
import com.example.social_network.domain.entity.User;
import com.example.social_network.event.RegistrationEventPublisher;
import com.example.social_network.service.ConfirmRegServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Controller
public class AuthController {

    @Autowired
    private final RegistrationEventPublisher eventPublisher;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private final ConfirmRegServiceImpl confirmRegServiceImpl;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public AuthController(RegistrationEventPublisher eventPublisher, UserServiceImpl userServiceImpl, ConfirmRegServiceImpl confirmRegServiceImpl, PasswordEncoder passwordEncoder) {

        this.eventPublisher = eventPublisher;
        this.userServiceImpl = userServiceImpl;
        this.confirmRegServiceImpl = confirmRegServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("/reg")
    public String registration(Model model, @ModelAttribute("badEmail") String badEmail, @ModelAttribute("wrong") String wrong,
                               @ModelAttribute("error") String error) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("badEmail", badEmail);
        model.addAttribute("wrong", wrong);
        model.addAttribute("user", userDTO);
        model.addAttribute("error", error);
        return "registration";
    }


    @PostMapping("/regandconfirm")
    public String registerUserAccount(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, RedirectAttributes attributes,
                                      HttpServletRequest request) throws IOException {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("error", "field cant be empty");
            attributes.addFlashAttribute("showError", "true");
            return "redirect:reg";
        }

        User user = new User();
        ImageInfo imageInfo = new ImageInfo();
        String password = userDTO.getPassword();

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("static/images/noo.jpg")).getFile());
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        imageInfo.setImageData(fileBytes);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        User registered = confirmRegServiceImpl.registerNewUserAccount(user);

        imageInfo.setName("image_" + userDTO.getName());
        imageInfo.setUser(registered);
        registered.setImageInfo(imageInfo);
        userServiceImpl.saveNew(registered);

        String appUrl = request.getContextPath();
        eventPublisher.publishEvent(appUrl, registered);

        return "successReg";
    }


}

