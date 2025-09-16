package com.techmanager.techmanager.user;

import com.techmanager.techmanager.user.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest req) {
    var created = service.create(req);
    return ResponseEntity.created(URI.create("/api/users/" + created.id())).body(created);
  }

  @GetMapping
  public List<UserResponse> list() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public UserResponse get(@PathVariable Long id) {
    return service.findById(id);
  }

  @PutMapping("/{id}")
  public UserResponse update(@PathVariable Long id, @RequestBody @Valid UserRequest req) {
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
