package by.itransition.site.controller;

import by.itransition.site.bean.Collection;
import by.itransition.site.bean.User;
import by.itransition.site.service.CollectionService;
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
public class CollectionController {
    private final CollectionService collectionService;

    @Value("${upload.path}")
    private String uploadPath;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping("/collections")
    public String getListOfCollectionsByDescCountItemOrder(
            @PageableDefault(size = 16, sort = "countItems", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model) {
        Page<Collection> page = collectionService.findAll(pageable);
        if (page.getSize() <= 16) {
            model.addAttribute("smallData", "smallData");
        }
        List<Collection> collections = page.getContent();
        model.addAttribute("page", page);
        model.addAttribute("collections", collections);
        model.addAttribute("user", user);
        return "collections";
    }

    @GetMapping("/user/profile/collection")
    public String getListOfCollectionsByUserId(
            @ModelAttribute("collection") Collection collection,
            @PageableDefault(size = 4, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user,
            Model model) {
        Page<Collection> page = collectionService.findByUserId(user.getId(), pageable);
        if (page.getSize() <= 4) {
            model.addAttribute("smallData", "smallData");
        }
        List<Collection> collections = page.getContent();
        model.addAttribute("page", page);
        model.addAttribute("collections", collections);
        return "collection";
    }

//    @GetMapping("/user/profile/collection")
//    public String getListOfCollectionsByUserIdDefault( @ModelAttribute("collection") Collection collection,
//                                                       @AuthenticationPrincipal User user,
//                                                       Model model) {
//        return getListOfCollectionsByUserId( collection,1,user,model);
//    }

    @PostMapping("/user/profile/collection")
    public String add(
            @ModelAttribute("collection") @Valid Collection collection,
            BindingResult bindingResult,
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file,
            Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "error");
            return "collection";
        }
        validateFileRequestFromCollection(collection, file);
        collection.setUser(user);
        collectionService.save(collection);
        return "redirect:/user/profile/collection";
    }


    @PostMapping("/user/profile/collection/remove")
    public String removeCollection(@RequestParam("collectionId") Collection collection) {
        collectionService.removeCollection(collection);
        return "redirect:/user/profile/collection";
    }

    @GetMapping("/user/profile/collection/edit/{collectionId}")
    public String getCollectionForUpdate(
            @ModelAttribute("collection") Collection collection,
            @PathVariable Long collectionId,
            Model model) {
        Optional<Collection> collectionFromDb = collectionService.findById(collectionId);
        model.addAttribute("collection", collectionFromDb.get());
        return "collectionEdit";
    }

    @PostMapping("/user/profile/collection/edit")
    public String updateCollection(
            @RequestParam("collectionId") Collection collectionFromDb,
            @ModelAttribute("collection") @Valid Collection collection,
            BindingResult bindingResult,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (bindingResult.hasErrors()) {
            return "collectionEdit";
        }
        validateFileRequestFromCollection(collection, file);
        collectionService.updateCollection(collectionFromDb, collection);
        return "redirect:/user/profile/collection";
    }

    private void validateFileRequestFromCollection(@ModelAttribute("collection") @Valid Collection collection, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFileName));
            collection.setFileName(resultFileName);
        }
    }

}
