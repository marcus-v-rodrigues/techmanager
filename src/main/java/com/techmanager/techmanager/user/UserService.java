package com.techmanager.techmanager.user;

import com.techmanager.techmanager.user.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
  private final UserRepository repo;
  private static final String USER_NOT_FOUND = "User %d not found";
  private static final String EMAIL_USED = "E-mail already used";

  public UserService(UserRepository repo) {
    this.repo = repo;
  }

  @Transactional
  public UserResponse create(UserRequest req) {
    if (repo.existsByEmail(req.email()))
      throw new IllegalArgumentException(EMAIL_USED);
    var saved = repo.save(UserMapper.toEntity(req));
    return UserMapper.toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> findAll() {
    return repo.findAll().stream().map(UserMapper::toResponse).toList();
  }

  @Transactional(readOnly = true)
  public UserResponse findById(Long id) {
    var u = repo.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.formatted(id)));
    return UserMapper.toResponse(u);
  }

  @Transactional
  public UserResponse update(Long id, UserRequest req) {
    var u = repo.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.formatted(id)));
    // prevent email collision with another user
    repo.findByEmail(req.email()).filter(other -> !other.getId().equals(id)).ifPresent(x -> {
      throw new IllegalArgumentException(EMAIL_USED);
    });
    UserMapper.update(u, req);
    return UserMapper.toResponse(u);
  }

  @Transactional
  public void delete(Long id) {
    if (!repo.existsById(id))
      throw new NotFoundException(USER_NOT_FOUND.formatted(id));
    repo.deleteById(id);
  }
}
