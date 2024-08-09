package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BalanceApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private AccountRepository accountRepository;

    private String jwtToken;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        authenticate();
    }

    private void authenticate() throws Exception {
        var loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"username\":\"admin\", \"password\":\"admin123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        String responseBody = loginResponse.getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        jwtToken = jsonResponse.get("token").asText();
    }

    @Order(1)
    @Test
    public void testCreateBalance_Success() throws Exception {
        Account account = new Account();
        account.setAccountID(3L);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/balances")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(account.getAccountID())))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("CR"));
    }

    @Test
    public void testGetBalanceByAccountId_Success() throws Exception {
        Long accountId = 1L;
        Balance balance = new Balance();
        balance.setBalanceID(1L);
        balance.setAccount(accountRepository.getById(accountId));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/balances/" + accountId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1500.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("credit"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account.accountID").value(accountId));
    }



    @Test
    public void testGetBalanceById_Success() throws Exception {

        Balance balance = new Balance();
        balance.setBalanceID(1L);

        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/balances/1")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balanceID").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(1500.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.indicator").value("credit"));
    }

}
