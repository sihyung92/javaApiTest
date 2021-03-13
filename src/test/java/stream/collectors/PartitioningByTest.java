package stream.collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stream.collectors.blog.BlogPost;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static stream.collectors.blog.BlogPostType.*;

public class PartitioningByTest {
    List<BlogPost> posts = Arrays.asList(
            new BlogPost("제목1", "작가1", GUIDE, 1),
            new BlogPost("제목2", "작가1", REVIEW, 2),
            new BlogPost("제목3", "작가1", REVIEW, 3),
            new BlogPost("제목4", "작가2", NEWS, 4),
            new BlogPost("제목5", "작가3", GUIDE, 5),
            new BlogPost("제목6", "작가4", REVIEW, 6),
            new BlogPost("제목7", "작가4", GUIDE, 7),
            new BlogPost("제목7", "작가4", GUIDE, 7)
    );

    @DisplayName("Collectors.PartitioningBy를 테스트한다.")
    @Test
    void partitioningByTest() {
        Map<Boolean, List<BlogPost>> collect = posts.stream()
                .collect(Collectors.partitioningBy(post -> post.getLikes() >= 4));

        assertEquals(collect.get(true).size(), 5);
        assertEquals(collect.get(false).size(), 3);
    }
}
