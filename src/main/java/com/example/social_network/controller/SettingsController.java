package com.example.social_network.controller;

import com.example.social_network.domain.dto.UserDTO;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("api/settings")
public class SettingsController {

    @Autowired
    private final UserServiceImpl userServiceImpl;

    public SettingsController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/showChangeSettings")
    public String settings(@AuthenticationPrincipal User user, @ModelAttribute("wrong") String wrong, @ModelAttribute("imageError") String imageError, @ModelAttribute("error") String error, @ModelAttribute("showError") String showError, Model model) {

        User userByName = userServiceImpl.getUserByName(user.getName());
        model.addAttribute("wrong", wrong);
        model.addAttribute("error", error);
        model.addAttribute("showError", showError);
        model.addAttribute("imageError", imageError);
        model.addAttribute("user", userByName);
        model.addAttribute("updateProfile", true);
        model.addAttribute("userDTO", new UserDTO());

        return "updateProfile";
    }

    @PostMapping("/changeSettings")
    public String updateSettings(@AuthenticationPrincipal User user,
                                 @Valid @ModelAttribute("data") UserDTO userDTO,
                                 BindingResult result,
                                 RedirectAttributes attributes,
                                 @RequestHeader(required = false) String referer) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("error", "field cant be empty");
            attributes.addFlashAttribute("showError", "true");
            UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
            return "redirect:" + components.getPath();
        }
        userServiceImpl.updateUserCredentials(user.getId(), userDTO);
        return "redirect:/login";
    }

}
