package com.librarymicroservices.userservice.controller;

import com.librarymicroservices.userservice.dto.UserDTO;
import com.librarymicroservices.userservice.model.UserEntity;
import com.librarymicroservices.userservice.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15");


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("John");
        userDTO.setIsTaken(false);

        UserEntity userEntity = UserDTO.toEntity(userDTO);
        userRepository.save(userEntity);

    }

    @BeforeAll
    public static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "name": "John",
                                "isTaken": false
                            }
                        ]
                        """));

    }

    @Test
    void shouldReturnUserById() throws Exception {
        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            {
                                "id": 1,
                                "name": "John",
                                "isTaken": false
                            }
                        """));
    }

    @Test
    void shouldCreateNewUser() throws Exception {
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                	"name" : "George"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/2"));

        //clear
        userRepository.deleteById(2L);
    }

    @Test
    void shouldDeleteUser() throws Exception {
        //given
        UserDTO userDTOForDelete = new UserDTO();
        userDTOForDelete.setId(3L);
        userDTOForDelete.setName("Pavel");
        userDTOForDelete.setIsTaken(false);

        UserEntity userEntityForDelete = UserDTO.toEntity(userDTOForDelete);
        userRepository.save(userEntityForDelete);

        //when
        mockMvc.perform(delete("/api/users/3").contentType((MediaType.APPLICATION_JSON)))
                //then
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        mockMvc.perform(put("/api/users").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                	"id" : 1,
                                	"name" : "Petya",
                                	"isTaken" : false
                                }
                                """))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldSetFlagBookTaken() throws Exception {
        mockMvc.perform(post("/api/users/taken/1/set").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldResetFlagBookTaken() throws Exception {
        mockMvc.perform(post("/api/users/taken/1/reset").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldCheckFlagBookTaken() throws Exception {
        mockMvc.perform(get("/api/users/taken/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("false"));
    }
}