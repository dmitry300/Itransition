package com.itransition.site.controller;

import com.itransition.site.bean.Role;
import com.itransition.site.bean.User;
import com.itransition.site.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String save(
            @RequestParam String userName,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.save(user, userName, form);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfile(
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String username,
            @RequestParam String password) {
        userService.updateProfile(user, username, password);
        return "redirect:/user/profile";
    }
}
