package stream.collectors.blog;

import java.util.Objects;

public class Tuple {
    BlogPostType type;
    String author;

    public Tuple(BlogPostType type, String author) {
        this.type = type;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        Tuple tuple = (Tuple) o;
        return type == tuple.type && Objects.equals(author, tuple.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, author);
    }
}
