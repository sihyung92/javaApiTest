package stream.collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (list, t) -> {
            System.out.println(Thread.currentThread().getId() + "accumulate ::: " + t);
            list.add(t);
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            System.out.println(Thread.currentThread().getId() + "combine ::: " + " " + list1 + "+" + list2);
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return (list) -> {
            System.out.println(Thread.currentThread().getId() + " finish :::" + list);
            return list;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
//        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT));
//        return Collections.unmodifiableSet(EnumSet.of(Characteristics.CONCURRENT,Characteristics.UNORDERED));
        return Collections.EMPTY_SET;
    }
}

class Main {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "f", "d", "b", "c", "e");
        Set<String> stringSet = new HashSet<>(strings);
        List<String> collect = stringSet.parallelStream()
                .map(s -> {
                    System.out.println(Thread.currentThread().getId() + "map ::: " + s);
                    return s;
                })
                .collect(new ToListCollector<>());
        System.out.println("result ::: " + collect);
    }
}