package com.itransition.site.repos;

import com.itransition.site.bean.dto.ItemDto;
import com.itransition.site.bean.Item;
import com.itransition.site.bean.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepo extends JpaRepository<Item, Long> {

    @Query("select new com.itransition.site.bean.dto.ItemDto(" +
            "   i, " +
            "   count(il), " +
            "   sum(case when il = :user then 1 else 0 end) > 0 " +
            ") " +
            "from Item i left join i.likes il " +
            "where i.id = :itemId " +
            "group by i")
    ItemDto findById(@Param("user") User user, @Param("itemId") Long itemId);

    @Query("select new com.itransition.site.bean.dto.ItemDto(" +
            "   i, " +
            "   count(il), " +
            "   sum(case when il = :user then 1 else 0 end) > 0 " +
            ") " +
            "from Item i left join i.likes il " +
            "group by i")
    Page<ItemDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("select new com.itransition.site.bean.dto.ItemDto(" +
            "   i, " +
            "   count(il), " +
            "   sum(case when il = :user then 1 else 0 end) > 0 " +
            ") " +
            "from Item i left join i.likes il " +
            "where i.collection.id = :collectionId " +
            "group by i")
    Page<ItemDto> findByCollectionId(@Param("collectionId") Long collectionId, @Param("user") User user, Pageable pageable);

    @Query("select new com.itransition.site.bean.dto.ItemDto(" +
            "   i, " +
            "   count(il), " +
            "   sum(case when il = :user then 1 else 0 end) > 0 " +
            ") " +
            "from Item i left join i.likes il " +
            "where i.collection.id = :collectionId and i.name = :name " +
            "group by i")
    Page<ItemDto> findByCollectionIdAndName(@Param("collectionId") Long collectionId, @Param("user") User user, @Param("name") String name, Pageable pageable);
}
