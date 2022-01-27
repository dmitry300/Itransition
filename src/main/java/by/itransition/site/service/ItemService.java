package by.itransition.site.service;

import by.itransition.site.bean.Collection;
import by.itransition.site.bean.Item;
import by.itransition.site.repos.ItemRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Iterable<Item> findByTag(String tag) {
        return itemRepo.findByTag(tag);
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepo.findAll(pageable);
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

    public Iterable<Item> findByCollectionId(Long collectionId) {
        return itemRepo.findByCollectionId(collectionId);
    }

    public void removeItem(Item item) {
        itemRepo.delete(item);
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
}
