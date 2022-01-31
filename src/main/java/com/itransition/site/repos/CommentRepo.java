package com.itransition.site.repos;

import com.itransition.site.bean.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    Iterable<Comment> findByItemId(Long itemId);
}
