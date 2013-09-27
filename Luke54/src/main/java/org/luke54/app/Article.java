package org.luke54.app;

/**
 * Created by hitobias on 13/9/5.
 */
public class Article {
    private String id;
    private String title;
    private String link;
    private String thumbImgSrc;
    private String description;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setThumbImgSrc(String thumbImgSrc) {
        this.thumbImgSrc = thumbImgSrc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getThumbImgSrc() {
        return thumbImgSrc;
    }

    public String getDescription() {
        return description;
    }

    public Article(String id, String title, String link, String thumbImgSrc, String description) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.thumbImgSrc = thumbImgSrc;
        this.description = description;
    }
}
