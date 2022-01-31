package com.itransition.site.service;

import com.itransition.site.bean.Collection;
import com.itransition.site.repos.CollectionRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class CollectionService {
    private final CollectionRepo collectionRepo;

    public CollectionService(CollectionRepo collectionRepo) {
        this.collectionRepo = collectionRepo;
    }

    public void save(Collection collection) {
        collectionRepo.save(collection);
    }

    public Page<Collection> findByUserId(Long userId, Pageable pageable) {
        return collectionRepo.findByUserId(userId, pageable);
    }

    public Optional<Collection> findById(Long id) {
        return collectionRepo.findById(id);
    }

    public void updateCollection(Collection collectionFromDb,Collection collection) {
        if (!StringUtils.isEmpty(collectionFromDb.getName()) && collection.getName() != null && !Objects.equals(collectionFromDb.getName(), collection.getName())) {
            collectionFromDb.setName(collection.getName());
        }
        if (!StringUtils.isEmpty(collectionFromDb.getTopic()) && collection.getTopic() != null && !Objects.equals(collectionFromDb.getTopic(), collection.getTopic())) {
            collectionFromDb.setTopic(collection.getTopic());
        }
        if (!StringUtils.isEmpty(collectionFromDb.getDescription()) && collection.getDescription() != null && !Objects.equals(collectionFromDb.getDescription(), collection.getDescription())) {
            collectionFromDb.setDescription(collection.getDescription());
        }
        if (!StringUtils.isEmpty(collection.getFileName()) && collection.getFileName() != null&& !Objects.equals(collectionFromDb.getFileName(), collection.getFileName())) {
            collectionFromDb.setFileName(collection.getFileName());
        }
        collectionRepo.save(collectionFromDb);
    }

    public void removeCollection(Collection collection) {
        collectionRepo.delete(collection);
    }

    public Page<Collection> findAll(Pageable pageable) {
        return collectionRepo.findAll(pageable);
    }
}
