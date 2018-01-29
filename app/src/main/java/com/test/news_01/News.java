package com.test.news_01;

// Класс новость
public class News {

    // Источник
    private String name;

    // Автор
    private String autor;

    // Название статьи
    private String title;

    // Описание статьи
    private String description;

    // Дата публикации
    private String publishedAt;

    // Ссылка на статью
    private String url;

    // Ссылка на изображение
    private String urlToImage;

    public News(String name, String autor, String title, String description, String publishedAt, String url, String urlToImage) {
        this.name = name;
        this.autor = autor;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.url = url;
        this.urlToImage = urlToImage;
    }

    public String getName() {
        return name;
    }

    public String getAutor() {
        return autor;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }
}

