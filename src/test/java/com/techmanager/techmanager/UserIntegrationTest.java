package com.techmanager.techmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmanager.techmanager.user.UserType;
import com.techmanager.techmanager.user.dto.UserRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

  static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16"));

  @DynamicPropertySource
  static void props(DynamicPropertyRegistry r) {
    pg.start();
    r.add("spring.datasource.url", pg::getJdbcUrl);
    r.add("spring.datasource.username", pg::getUsername);
    r.add("spring.datasource.password", pg::getPassword);
    r.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
  }

  @Autowired
  MockMvc mvc;
  @Autowired
  ObjectMapper om;

  @AfterAll
  static void stop() {
    pg.stop();
  }

  @Test
  void create_user_201() throws Exception {
    var req = new UserRequest("Nome", "x@y.com", "+55 11 99999-9999",
        LocalDate.parse("1990-01-01"), UserType.ADMIN);

    mvc.perform(post("/api/users")
        .contentType("application/json")
        .content(om.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.email").value("x@y.com"));
  }
}
