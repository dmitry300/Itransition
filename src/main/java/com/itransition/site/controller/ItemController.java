package com.itransition.site.controller;

import com.itransition.site.bean.Collection;
import com.itransition.site.bean.Item;
import com.itransition.site.bean.User;
import com.itransition.site.bean.dto.ItemDto;
import com.itransition.site.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Controller
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String getListOfItems(
            @PageableDefault(size = 16, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model) {
        Page<ItemDto> page = itemService.findAll(pageable, user);
        List<ItemDto> items = page.getContent();
        if (page.getSize() <= 16) {
            model.addAttribute("smallData", "smallData");
        }
        model.addAttribute("page", page);
        model.addAttribute("items", items);
        model.addAttribute("user", user);
        return "index";
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/collection/items/remove")
    public String removeItem(@RequestParam("itemId") Item item) {
        itemService.removeItem(item);
        return "redirect:/user/profile/collection/item/" + item.getCollection().getId();
    }

    @GetMapping("/user/profile/collection/items/{collectionId}")
    public String getItemsByCollection(
            @AuthenticationPrincipal User user,
            @PathVariable Long collectionId,
            @RequestParam(required = false) String filterName,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "desc") String sortDir,
            @ModelAttribute("item") Item item,
            Model model) {
        int pageSize = 4;
        Page<ItemDto> page = itemService.findByCollectionIdAndFilter(collectionId, user, pageNo, pageSize, sortField, sortDir, filterName);
        List<ItemDto> items = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("filter", filterName);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("page", page);
        model.addAttribute("items", items);
        return "items";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/collection/items")
    public String addItem(
            @RequestParam("collectionId") Collection collection,
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "error");
            return "items";
        }
        validateFile(item, file);
        item.setCollection(collection);
        itemService.save(item);
        return "redirect:/user/profile/collection/items/" + collection.getId();
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @GetMapping("/user/profile/collection/items/edit/{itemId}")
    public String getItemForEdit(
            @ModelAttribute("item") Item item,
            @PathVariable Long itemId,
            Model model) {
        Optional<Item> itemFromDb = itemService.findById(itemId);
        itemFromDb.ifPresent(value -> model.addAttribute("item", value));
        return "itemsEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @PostMapping("/user/profile/collection/items/edit")
    public String editItem(
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            @RequestParam("itemId") Item itemFromDb,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            return "itemsEdit";
        }
        validateFile(item, file);
        itemService.updateItem(itemFromDb, item);
        return "redirect:/user/profile/collection/item/" + itemFromDb.getCollection().getId();
    }
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CLIENT')")
    @GetMapping("/{itemId}/like")
    public String likeItem(
            @AuthenticationPrincipal User user,
            @PathVariable("itemId") Item item,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer) {
        Set<User> likes = item.getLikes();
        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }
        item.setLikes(likes);
        itemService.save(item);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .forEach(redirectAttributes::addAttribute);
        return "redirect:" + components.getPath();
    }

    private void validateFile(@ModelAttribute("item") @Valid Item item, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            item.setFileName(resultFileName);
        }
    }
}