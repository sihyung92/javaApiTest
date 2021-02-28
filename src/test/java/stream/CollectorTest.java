package stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper)
 * Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction)
 * Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction, Supplier<M> mapSupplier)
 * */
public class CollectorTest {
    List<Book> books;

    @BeforeEach
    void setUp() {
        books = new ArrayList<>();
        books.add(new Book("The Fellowship of the Ring", 1954, "0395489318"));
        books.add(new Book("The Two Towers", 1954, "0345339711"));
        books.add(new Book("The Return of the King", 1955, "0618129111"));
    }

    @DisplayName("Isbn가 key, 책 이름이 value인 맵 생성")
    @Test
    void listToMapTest() {
        Map<String, String> IsbnAndName = books.stream()
                .collect(Collectors.toMap(Book::getIsbn, Book::getName));

        int expectedSize = 3;

        assertEquals(IsbnAndName.size(), expectedSize);
    }

    @DisplayName("동일한 key가 있을 때 오류가 나는지 확인")
    @Test
    void whenMapHasDuplicatedKeyTest() {
        assertThrows(IllegalStateException.class,
                () -> books.stream()
                        .collect(Collectors.toMap(Book::getReleaseYear, Function.identity())));
    }

    @DisplayName("키 중복시 병합 로직도 추가한 toMap테스트")
    @Test
    void listToMapWithDupKeyTest() {
        Map<Integer, Book> releaseYearAndBook = books.stream()
                .collect(Collectors.toMap(Book::getReleaseYear, Function.identity(),
                        (existing, replacement) -> existing));

        assertEquals(releaseYearAndBook.size(), 2);
        assertEquals(releaseYearAndBook.get(1954).getName(), "The Fellowship of the Ring");
    }

    @DisplayName("Hash Map이 아닌 다른 Map으로 생성")
    @Test
    void listToConcurrentMapTest() {
        ConcurrentHashMap<Integer, Book> concurrentHashMap = books.stream()
                .collect(Collectors.toMap(Book::getReleaseYear, Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));

        assertTrue(concurrentHashMap instanceof ConcurrentHashMap);
    }
}

class Book {
    private String name;
    private int releaseYear;
    private String isbn;

    public Book(String name, int releaseYear, String isbn) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
