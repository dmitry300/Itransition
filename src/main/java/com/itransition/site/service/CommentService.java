package com.itransition.site.service;

import com.itransition.site.bean.Comment;
import com.itransition.site.repos.CommentRepo;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CommentService {
    private final CommentRepo commentRepo;

    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Iterable<Comment> findByItemId(Long itemId) {
        return commentRepo.findByItemId(itemId);
    }

    public void save(Comment comment) {
        comment.setDate(LocalDateTime.now());
        commentRepo.save(comment);
    }

    public void update(Comment comment, Comment commentFromDb) {
        if (!StringUtils.isEmpty(commentFromDb.getText()) && commentFromDb.getText() != null && !Objects.equals(commentFromDb.getText(), comment.getText())) {
            commentFromDb.setText(comment.getText());
        }
        commentFromDb.setText(comment.getText());
        commentRepo.save(commentFromDb);
    }

    public void remove(Comment comment) {
        commentRepo.delete(comment);
    }
}
