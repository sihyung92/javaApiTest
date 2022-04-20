package swagger.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="hello", description = "hello controller")
@RestController
public class HelloController {

  @GetMapping("/hello/{id}")
  public ResponseEntity<String> getHello(@RequestParam String param1, @PathVariable Long id){
    return ResponseEntity.ok("OK");
  }

  @DeleteMapping("/hello")
  public void deleteHello(){

  }
}
