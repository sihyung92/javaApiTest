package stream.collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilteringTest {
    @DisplayName("Collectors.filtering을 테스트한다.")
    @Test
    void filteringTest() {
        //public static <T,A,R> Collector<T,?,R> filtering(Predicate<? super T> predicate, Collector<? super T,A,R> downstream)
        List<Integer> list = IntStream.of(2, 4, 6, 8, 10, 12)
                .boxed()
                .collect(Collectors.filtering(i -> i % 4 == 0,
                        Collectors.toList()));
        assertTrue(list.size() == 3 && list.containsAll(Arrays.asList(4, 8, 12)));
    }
}
