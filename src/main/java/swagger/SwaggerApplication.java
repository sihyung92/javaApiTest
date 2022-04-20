package swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 실행 후 <a href="http://localhost:8080/swagger-ui.html">localhost:8080/swagger-ui.html</a> 확인
 */
@SpringBootApplication
public class SwaggerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SwaggerApplication.class, args);
  }
}
