package com.techmanager.techmanager;

import com.techmanager.techmanager.user.NotFoundException;
import com.techmanager.techmanager.user.User;
import com.techmanager.techmanager.user.UserRepository;
import com.techmanager.techmanager.user.UserService;
import com.techmanager.techmanager.user.UserType;
import com.techmanager.techmanager.user.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
  @Test
  void create_ok() {
    var repo = Mockito.mock(UserRepository.class);
    when(repo.existsByEmail("a@b.com")).thenReturn(false);
    when(repo.save(any())).thenAnswer(inv -> {
      var u = (User) inv.getArgument(0);
      u.setId(1L);
      return u;
    });

    var service = new UserService(repo);
    var req = new UserRequest("Nome", "a@b.com", "+55 11 99999-9999", LocalDate.parse("1990-01-01"), UserType.ADMIN);
    var res = service.create(req);

    assertNotNull(res.id());
    assertEquals("a@b.com", res.email());
  }

  @Test
  void findById_404() {
    var repo = Mockito.mock(UserRepository.class);
    when(repo.findById(123L)).thenReturn(Optional.empty());
    var service = new UserService(repo);
    assertThrows(NotFoundException.class, () -> service.findById(123L));
  }
}
