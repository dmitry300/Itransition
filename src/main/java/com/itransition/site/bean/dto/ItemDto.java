package com.itransition.site.bean.dto;

import com.itransition.site.bean.Collection;
import com.itransition.site.bean.Item;

import java.time.LocalDate;

public class ItemDto {
    private Long id;
    private String name;
    private String tag;
    private String text1;
    private String text2;
    private String text3;
    private LocalDate date1;
    private LocalDate date2;
    private LocalDate date3;
    private String fileName;
    private Collection collection;
    private Long likes;
    private Boolean isLiked;

    public ItemDto() {
    }

    public ItemDto(Item item, Long likes, Boolean isLiked) {
        this.id = item.getId();
        this.name = item.getName();
        this.tag = item.getTag();
        this.text1 = item.getText1();
        this.text2 = item.getText2();
        this.text3 = item.getText3();
        this.date1 = item.getDate1();
        this.date2 = item.getDate2();
        this.date3 = item.getDate3();
        this.fileName = item.getFileName();
        this.collection = item.getCollection();
        this.likes = likes;
        this.isLiked = isLiked;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }

    public LocalDate getDate1() {
        return date1;
    }

    public LocalDate getDate2() {
        return date2;
    }

    public LocalDate getDate3() {
        return date3;
    }

    public String getFileName() {
        return fileName;
    }

    public Collection getCollection() {
        return collection;
    }

    public Long getLikes() {
        return likes;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    @Override
    public String toString() {
        return "ItemDto{" +
                "id=" + id +
                ", likes=" + likes +
                ", isLiked=" + isLiked +
                '}';
    }
}
