package com.itransition.site.bean;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, max = 20, message = "Name should be between 3 and 20 characters")
    private String name;

    @Size(min = 3, max = 20, message = "Topic should be between 3 and 20 characters")
    @NotEmpty(message = "Topic cannot be empty")
    private String topic;

    @Size(min = 5, max = 255, message = "Description should be between 5 and 255 characters")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "count_items")
    private Long countItems;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToMany
//    @JoinColumn(name = "user_id")
//    private Set<Item> items;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountItems() {
        return countItems;
    }

    public void setCountItems(Long countItems) {
        this.countItems = countItems;
    }
}