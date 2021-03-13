package stream.collectors.blog;

import java.util.Objects;

public class BlogPost {
    String title;
    String author;
    BlogPostType type;
    int likes;

    public BlogPost(String title, String author, BlogPostType type, int likes) {
        this.title = title;
        this.author = author;
        this.type = type;
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlogPost)) return false;
        BlogPost blogPost = (BlogPost) o;
        return getLikes() == blogPost.getLikes() && Objects.equals(getTitle(), blogPost.getTitle()) && Objects.equals(getAuthor(), blogPost.getAuthor()) && getType() == blogPost.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthor(), getType(), getLikes());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BlogPostType getType() {
        return type;
    }

    public void setType(BlogPostType type) {
        this.type = type;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
