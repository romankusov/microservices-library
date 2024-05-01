package com.librarymicroservices.orderservice;

import com.librarymicroservices.orderservice.client.StorageServiceClient;
import com.librarymicroservices.orderservice.client.UserServiceClient;
import com.librarymicroservices.orderservice.model.Order;
import com.librarymicroservices.orderservice.model.OrderStatus;
import com.librarymicroservices.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

    @MockBean
    private StorageServiceClient storageServiceClient;
    @MockBean
    private UserServiceClient userServiceClient;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        orderRepository.save(new Order(1L, 1L, 1L, OrderStatus.CREATED));
    }

    @Test
    void shouldGetAllOrders() throws Exception {
        mvc.perform(get("/api/orders").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                "id": 1,
                                "userId": 1,
                                "bookId": 1,
                                "orderStatus": "CREATED"
                            }
                        ]
                        """));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        mvc.perform(get("/api/orders/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                                "id": 1,
                                "userId": 1,
                                "bookId": 1,
                                "orderStatus": "CREATED"
                        }
                        """));
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // Arrange
        when(storageServiceClient.getBookQuantity(1L)).thenReturn(1L);
        when(userServiceClient.checkBookTaken(1L)).thenReturn(false);

        // act + assert
        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                	"bookId" : 1,
                                	"userId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "api/orders/2"));
        // clear
        orderRepository.deleteById(2L);
    }

    @Test
    void shouldReturnBook() throws Exception {
        // Arrange
        when(userServiceClient.checkBookTaken(1L)).thenReturn(true);

        // act +  assert
        mvc.perform(post("/api/orders/return/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }
}
