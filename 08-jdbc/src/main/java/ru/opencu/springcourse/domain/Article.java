package ru.opencu.springcourse.domain;

import java.util.ArrayList;
import java.util.List;

public class Article {

    private Long id;
    private String title;
    private String text;
    private String venue;
    private List<String> authors = new ArrayList<>();

    public Article() {}

    public Article(String title, String text, String venue) {
        this.title = title;
        this.text = text;
        this.venue = venue;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public List<String> getAuthors() { return authors; }
    public void setAuthors(List<String> authors) { this.authors = authors; }

    @Override
    public String toString() {
        return "Article{id=%d, title='%s', venue='%s', authors=%s}"
                .formatted(id, title, venue, authors);
    }
}
