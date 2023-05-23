package com.example.social_network.controller;


import com.example.social_network.domain.entity.Comment;
import com.example.social_network.domain.entity.Message;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.CommentServiceImpl;
import com.example.social_network.service.MessageServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


@Controller
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final CommentServiceImpl commentServiceImpl;
    @Autowired
    private final MessageServiceImpl messageServiceImpl;

    public CommentController(UserServiceImpl userService, CommentServiceImpl commentServiceImpl, MessageServiceImpl messageServiceImpl) {
        this.userService = userService;
        this.commentServiceImpl = commentServiceImpl;
        this.messageServiceImpl = messageServiceImpl;
    }


    @GetMapping("/createCommentForm/{messageId}")
    public String startingPage(@AuthenticationPrincipal User user, Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size, @PathVariable Long messageId) {
        User currentUser = userService.getUserByName(user.getName());
        Message messageGotById = messageServiceImpl.getMessageID(messageId);
        Page<Comment> comments = commentServiceImpl.showComments(messageGotById, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("user", currentUser);
        model.addAttribute("messageGot", messageGotById);

        Comment comment = new Comment();
        comment.setMsg(messageGotById);
        comment.setAuthor(user);

        model.addAttribute("comment", comment);
        model.addAttribute("messageId", messageId);
        model.addAttribute("paginationCase", "commentController");
        model.addAttribute("comments", comments);
        model.addAttribute("totalPages", comments.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        return "listComments";
    }


    @PostMapping("/commentMessage/{id}")
    public String createComment(@AuthenticationPrincipal User user, @RequestParam("q") String comment, @PathVariable Long id,
                                RedirectAttributes attributes, @RequestHeader(required = false) String referer) {

        commentServiceImpl.createComment(user.getName(), comment, id);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        return "redirect:" + components.getPath();
    }

}
