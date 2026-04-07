package ru.opencu.springcourse.domain;

public class ArticleReview {

    private Long id;
    private String text;
    private Long articleId;

    public ArticleReview() {}

    public ArticleReview(String text, Long articleId) {
        this.text = text;
        this.articleId = articleId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Long getArticleId() { return articleId; }
    public void setArticleId(Long articleId) { this.articleId = articleId; }

    @Override
    public String toString() {
        return "ArticleReview{id=%d, articleId=%d, text='%s'}"
                .formatted(id, articleId, text);
    }
}
