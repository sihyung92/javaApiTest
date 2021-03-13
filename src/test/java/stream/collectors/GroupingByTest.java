package stream.collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stream.collectors.blog.BlogPost;
import stream.collectors.blog.BlogPostType;
import stream.collectors.blog.Tuple;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static stream.collectors.blog.BlogPostType.*;

public class GroupingByTest {
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

    @DisplayName("keyMapper만 주어서 맵 생성 테스트")
    @Test
    void groupingByAuthor() {
        Map<String, List<BlogPost>> groupByAuthor = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor));
        int expectedAuthorsSize = 4;

        assertEquals(groupByAuthor.size(), expectedAuthorsSize);
    }

    @DisplayName("복잡한 keyMapper 활용하여 맵 생성")
    @Test
    void gropingByTuple(){
        Map<Tuple, List<BlogPost>> postsPerTypeAndAuthor = posts.stream()
                .collect(Collectors.groupingBy(post -> new Tuple(post.getType(), post.getAuthor())));

        assertEquals(postsPerTypeAndAuthor.get(new Tuple(GUIDE,"작가1")).size(), 1);
    }

    @DisplayName("value를 List가 아니라 원하는 방식으로 mapping")
    @Test
    void groupingByModifyingValueType() {
        Map<BlogPostType, Set<BlogPost>> postsPerType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType, Collectors.toSet()));
        int expectedAuthorsSize = 3; //4개 중 중복건 2개

        assertEquals(postsPerType.get(GUIDE).size(), expectedAuthorsSize);
    }

    @DisplayName("여러 칼럼을 기준으로 그룹핑")
    @Test
    void groupingByMultipleColumn() {
        Map<String, Map<BlogPostType, List<BlogPost>>> groupingAuthorThanType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor, Collectors.groupingBy(BlogPost::getType)));
        int expectedSize = 2;

        assertEquals(groupingAuthorThanType.get("작가1").get(REVIEW).size(), expectedSize);
    }

    @DisplayName("그룹한 결과값의 개수 구하기")
    @Test
    void gettingGroupingResultsCount() {
        Map<String, Long> averageLikesPerAuthor = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor, Collectors.counting()));
        int expectedLikesCount = 3;

        assertEquals(averageLikesPerAuthor.get("작가1"), expectedLikesCount);
    }

    @DisplayName("그룹한 결과값의 평균 구하기")
    @Test
    void gettingGroupingResultsAverage() {
        Map<String, Double> averageLikesPerAuthor = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor, Collectors.averagingInt(BlogPost::getLikes)));
        int expectedLikesCount = 2;

        assertEquals(averageLikesPerAuthor.get("작가1"), expectedLikesCount);
    }


    @DisplayName("그룹한 결과값의 합계 구하기")
    @Test
    void gettingGroupingResultsSum() {
        Map<String, Integer> averageLikesPerAuthor = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getAuthor, Collectors.summingInt(BlogPost::getLikes)));
        int expectedLikesCount = 6;

        assertEquals(averageLikesPerAuthor.get("작가1"), expectedLikesCount);
    }

    @DisplayName("그룹 결과의 최대,최소 요소 구하기")
    @Test
    void gettingGroupingResultsMaxAndMin() {
        Map<BlogPostType, Optional<BlogPost>> maxLikesPerPostType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        Collectors.maxBy(Comparator.comparingInt(BlogPost::getLikes))));

        Map<BlogPostType, Optional<BlogPost>> minLikesPerPostType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        Collectors.minBy(Comparator.comparingInt(BlogPost::getLikes))));

        int max = 7;
        int min = 1;
        assertEquals(maxLikesPerPostType.get(GUIDE).get().getLikes(), max);
        assertEquals(minLikesPerPostType.get(GUIDE).get().getLikes(), min);
    }

    @DisplayName("value를 커스텀하여 저장하기")
    @Test
    void mappingGroupedResultsToDifferentType() {
        Map<BlogPostType, String> postsPerType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        Collectors.mapping(BlogPost::getTitle, Collectors.joining(", ", "Post titles: [", "]"))));
        String expectedResult = "Post titles: [제목2, 제목3, 제목6]";

        assertEquals(postsPerType.get(REVIEW), expectedResult);
    }

    @DisplayName("다른 map으로 저장하기")
    @Test
    void groupingByAnotherMap() {
        EnumMap<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
                .collect(Collectors.groupingBy(BlogPost::getType,
                        () -> new EnumMap<>(BlogPostType.class), Collectors.toList()));
        int expectedGuideSize = 4;

        assertEquals(postsPerType.get(GUIDE).size(), expectedGuideSize);
    }

    @DisplayName("Thread-safe하게 만들기")
    @Test
    void getConcurrentHashMap() {
        ConcurrentMap<BlogPostType, List<BlogPost>> postsPerType = posts.parallelStream()
                .collect(Collectors.groupingByConcurrent(BlogPost::getType));

        assertTrue(postsPerType instanceof ConcurrentHashMap);
    }
}

