package com.itransition.site.service;

import com.itransition.site.bean.dto.ItemDto;
import com.itransition.site.bean.Collection;
import com.itransition.site.bean.Item;
import com.itransition.site.bean.User;
import com.itransition.site.repos.ItemRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepo itemRepo;

    public ItemService(ItemRepo itemRepo) {
        this.itemRepo = itemRepo;
    }

    public Page<ItemDto> findAll(Pageable pageable, User user) {
        return itemRepo.findAll(pageable, user);
    }

    public void save(Item item) {
        Collection collection = item.getCollection();
        if (collection.getCountItems() == null) {
            collection.setCountItems(1L);
        } else {
            long countItems = collection.getCountItems();
            collection.setCountItems(++countItems);
        }
        itemRepo.save(item);
    }

    public void removeItem(Item item) {
        itemRepo.delete(item);
    }

    public ItemDto findById(User user, Long itemId) {
        return itemRepo.findById(user, itemId);
    }

    public Optional<Item> findById(Long itemId) {
        return itemRepo.findById(itemId);
    }

    public void updateItem(Item itemFromDb, Item item) {
        if (!StringUtils.isEmpty(item.getName()) && item.getName() != null && !Objects.equals(item.getName(), itemFromDb.getName())) {
            itemFromDb.setName(item.getName());
        }
        if (!StringUtils.isEmpty(item.getTag()) && item.getTag() != null && !Objects.equals(item.getTag(), itemFromDb.getTag())) {
            itemFromDb.setTag(item.getTag());
        }
        if (!StringUtils.isEmpty(item.getText1()) && item.getText1() != null && !Objects.equals(item.getText1(), itemFromDb.getText1())) {
            itemFromDb.setText1(item.getText1());
        }
        if (!StringUtils.isEmpty(item.getText2()) && item.getText2() != null && !Objects.equals(item.getText2(), itemFromDb.getText2())) {
            itemFromDb.setText2(item.getText2());
        }
        if (!StringUtils.isEmpty(item.getText3()) && item.getText3() != null && !Objects.equals(item.getText3(), itemFromDb.getText3())) {
            itemFromDb.setText3(item.getText3());
        }
        if (item.getDate1() != null && !item.getDate1().isEqual(itemFromDb.getDate1())) {
            itemFromDb.setDate1(item.getDate1());
        }
        if (item.getDate2() != null && !item.getDate2().isEqual(itemFromDb.getDate2())) {
            itemFromDb.setDate2(item.getDate2());
        }
        if (item.getDate3() != null && !item.getDate3().isEqual(itemFromDb.getDate3())) {
            itemFromDb.setDate3(item.getDate3());
        }
        if (!StringUtils.isEmpty(item.getFileName()) && item.getFileName() != null) {
            itemFromDb.setFileName(item.getFileName());
        }
        itemRepo.save(itemFromDb);
    }

    public Page<ItemDto> findByCollectionIdAndFilter(Long collectionId, User user, int pageNo, int pageSize, String sortField, String sortDirection, String filterName) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        if (filterName != null && !StringUtils.isEmpty(filterName)) {
            Pageable pageable = PageRequest.of(0, pageSize, sort);
            return itemRepo.findByCollectionIdAndName(collectionId, user, filterName, pageable);
        } else {
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            return itemRepo.findByCollectionId(collectionId, user, pageable);
        }
    }
}
