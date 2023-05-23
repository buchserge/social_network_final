package com.example.social_network.controller;


import com.example.social_network.domain.entity.User;
import com.example.social_network.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping ("/api")
public class FriendListController {
    @Autowired
    private UserServiceImpl userServiceImpl;


    public FriendListController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }


    @GetMapping("/showFriends/{id}")
    public String displayFriends(@PathVariable Long id,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 Model model) {

        User userById = userServiceImpl.findById(id);

        PagedListHolder pagedListHolder = new PagedListHolder(userById.getFriends().stream().toList());
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);

        model.addAttribute("paginationCase", "friendsController");
        model.addAttribute("totalPages", pagedListHolder.getPageCount());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", pagedListHolder.getPageSize());
        model.addAttribute("friends", pagedListHolder.getPageList());
        model.addAttribute("userById", userById);
        return "friendList";
    }



}
