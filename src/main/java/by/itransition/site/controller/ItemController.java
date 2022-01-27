package by.itransition.site.controller;

import by.itransition.site.bean.Collection;
import by.itransition.site.bean.Item;
import by.itransition.site.bean.User;
import by.itransition.site.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
        Page<Item> page = itemService.findAll(pageable);
        List<Item> items = page.getContent();
        if (page.getSize() <= 16) {
            model.addAttribute("smallData", "smallData");
        }
        model.addAttribute("page", page);
        model.addAttribute("items", items);
        model.addAttribute("user", user);
        return "index";
    }

    //    @GetMapping("/main")
//    public String main(@RequestParam(required = false) String tag, Model model) {
//        Iterable<Item> items;
//        if (tag != null && !tag.isEmpty()) {
//            items = itemService.findByTag(tag);
//        } else {
//            items = itemService.findAll();
//        }
//        model.addAttribute("items", items);
//        model.addAttribute("tag", tag);
//        return "main";
//    }

    @PostMapping("/user/profile/collection/item/remove")
    public String removeItem(@RequestParam("itemId") Item item) {
        itemService.removeItem(item);
        return "redirect:/user/profile/collection/item/" + item.getCollection().getId();
    }

    @GetMapping("/user/profile/collection/item/{collectionId}")
    public String getItemsByCollection(
            @PathVariable Long collectionId,
            @ModelAttribute("item") Item item,
            Model model) {
        Iterable<Item> items = itemService.findByCollectionId(collectionId);
        model.addAttribute("items", items);
        return "item";
    }

    @PostMapping("/user/profile/collection/item")
    public String addItem(
            @RequestParam("collectionId") Collection collection,
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "error");
            return "item";
        }
        validateFile(item, file);
        item.setCollection(collection);
        itemService.save(item);
        return "redirect:/user/profile/collection/item/" + collection.getId();
    }

    @GetMapping("/user/profile/collection/item/edit/{itemId}")
    public String getItemForEdit(
            @ModelAttribute("item") Item item,
            @PathVariable Long itemId,
            Model model) {
        Optional<Item> itemFromDb = itemService.findById(itemId);
        model.addAttribute("item", itemFromDb.get());
        return "itemEdit";
    }

    @PostMapping("/user/profile/collection/item/edit")
    public String editItem(
            @ModelAttribute("item") @Valid Item item,
            BindingResult bindingResult,
            @RequestParam("itemId") Item itemFromDb,
            @RequestParam("file") MultipartFile file) throws IOException
    {
        if (bindingResult.hasErrors()) {
            return "itemEdit";
        }
        validateFile(item, file);
        itemService.updateItem(itemFromDb, item);
        return "redirect:/user/profile/collection/item/" + itemFromDb.getCollection().getId();
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