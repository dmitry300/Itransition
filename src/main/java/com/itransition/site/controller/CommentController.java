package com.itransition.site.controller;

import com.itransition.site.bean.Comment;
import com.itransition.site.bean.Item;
import com.itransition.site.bean.User;
import com.itransition.site.bean.dto.ItemDto;
import com.itransition.site.service.CommentService;
import com.itransition.site.service.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final ItemService itemService;

    public CommentController(CommentService commentService, ItemService itemService) {
        this.commentService = commentService;
        this.itemService = itemService;
    }


    @GetMapping("/user/profile/item/{itemId}")
    public String itemView(
            @AuthenticationPrincipal User user,
            @ModelAttribute("comment") Comment comment,
            @PathVariable("itemId") Item item,
            Model model) {
        ItemDto itemDto = itemService.findById(user, item.getId());
        Iterable<Comment> comments = commentService.findByItemId(item.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("item", itemDto);
        return "itemInProfile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/item")
    public String add(@AuthenticationPrincipal User user,
                      @ModelAttribute("comment") @Valid Comment comment,
                      BindingResult bindingResult,
                      @RequestParam("itemId") Item item,
                      Model model) {
        if (bindingResult.hasErrors()) {
            ItemDto itemDto = itemService.findById(user, item.getId());
            model.addAttribute("item", itemDto);
            return "itemInProfile";
        }
        comment.setUser(user);
        comment.setItem(item);
        commentService.save(comment);
        return "redirect:/user/profile/item/" + item.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @GetMapping("/user/profile/item/comment/{commentId}")
    public String itemViewEdit(
            @ModelAttribute("comment") Comment comment,
            @PathVariable("commentId") Comment commentFromDb,
            Model model) {
        model.addAttribute("comment", commentFromDb);
        return "commentEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/item/comment/update")
    public String update(@AuthenticationPrincipal User user,
                         @ModelAttribute("comment") @Valid Comment comment,
                         BindingResult bindingResult,
                         @RequestParam("commentId") Comment commentFromDb,
                         Model model) {
        if (bindingResult.hasErrors()) {
            ItemDto itemDto = itemService.findById(user, commentFromDb.getItem().getId());
            model.addAttribute("item", itemDto);
            return "commentEdit";
        }
        commentService.update(comment, commentFromDb);
        return "redirect:/user/profile/item/" + commentFromDb.getItem().getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/item/comment/remove")
    public String remove(@RequestParam("commentId") Comment comment) {
        commentService.remove(comment);
        return "redirect:/user/profile/item/" + comment.getItem().getId();
    }
}
