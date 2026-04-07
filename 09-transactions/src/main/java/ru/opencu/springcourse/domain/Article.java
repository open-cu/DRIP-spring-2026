package ru.opencu.springcourse.domain;

public class Article {

    private Long id;
    private String title;
    private String text;
    private ArticleStatus status = ArticleStatus.DRAFT;

    public Article() {}

    public Article(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }

    public String getTitle()                { return title; }
    public void setTitle(String title)      { this.title = title; }

    public String getText()                 { return text; }
    public void setText(String text)        { this.text = text; }

    public ArticleStatus getStatus()              { return status; }
    public void setStatus(ArticleStatus status)   { this.status = status; }

    @Override
    public String toString() {
        return "Article{id=%d, title='%s', status=%s}".formatted(id, title, status);
    }
}
