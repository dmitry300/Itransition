package by.itransition.site.repos;

import by.itransition.site.bean.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item, Long> {
    List<Item> findByTag(String tag);

    List<Item> findByCollectionId(Long collectionId);
}
