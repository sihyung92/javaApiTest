package excel.poi;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Student {
    private final String name;
    private final int number;
    private final boolean male;
    private final double score;
    private final LocalDateTime startDate;
}
