package by.itransition.site.repos;

import by.itransition.site.bean.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepo extends JpaRepository<Collection, Long> {

   Page<Collection> findByUserId(Long userId, Pageable pageable);

    Page<Collection> findAll(Pageable pageable);
}
