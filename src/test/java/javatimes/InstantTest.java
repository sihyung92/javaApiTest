package javatimes;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class InstantTest {

  @DisplayName("truncate 메서드를 테스트한다.")
  @Test
  void truncateTest() {
    // given
    Instant now = Instant.now();
    // when
    Instant dayStart = now.truncatedTo(ChronoUnit.DAYS);
    // then
    Instant dayEnd = dayStart.plusMillis(1000 * (60 * 60 * 24) - 10);

    log.info(now.toString());
    log.info(dayStart.toString());
    log.info(dayEnd.toString());
  }
}
