package com.practice.project06;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.project06.account.Account;
import com.practice.project06.account.AccountRepository;
import com.practice.project06.balance.BalanceRepository;
import com.practice.project06.transaction.Transaction;
import com.practice.project06.transaction.TransactionController;
import com.practice.project06.transaction.TransactionDTO;
import com.practice.project06.transaction.TransactionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

import java.math.BigDecimal;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    public void testGetTransactionsByAccountId_Success() throws Exception {
        Long accountId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/transactions/by-account")
                        .param("id", accountId.toString())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }


    @Test
    public void testCreateTransaction_Success() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "123456";
        BigDecimal amount = BigDecimal.valueOf(120.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount.intValue()));
    }

//    @Order(2)
//    @Test
//    public void testCreateTransaction_InvalidFromAccount() throws Exception {
//        Long fromAccountID = 7L;
//        String toAccountNumber = "123456";
//        BigDecimal amount = BigDecimal.valueOf(100.00);
//
//        TransactionDTO transactionDTO = new TransactionDTO();
//        transactionDTO.setFromAccountID(fromAccountID);
//        transactionDTO.setToAccountNumber(toAccountNumber);
//        transactionDTO.setAmount(amount);
//        transactionDTO.setIndicator("credit");
//
//        when(accountRepository.findById(fromAccountID)).thenReturn(Optional.empty());
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + jwtToken)
//                        .content(objectMapper.writeValueAsString(transactionDTO)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid from account ID"));
//    }

    @Test
    public void testCreateTransaction_InvalidToAccount() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "ACC12334";
        BigDecimal amount = BigDecimal.valueOf(100.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No value present"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Order(1)
    @Test
    public void testCreateTransaction_InsufficientBalance() throws Exception {
        Long fromAccountID = 2L;
        String toAccountNumber = "12345";
        BigDecimal amount = BigDecimal.valueOf(1000000.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                //.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Low balance"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Order(6)
    @Test
    public void testCreateTransaction_InvalidToken() throws Exception {
        Long fromAccountID = 1L;
        String toAccountNumber = "ACC123";
        BigDecimal amount = BigDecimal.valueOf(1000.00);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountID(fromAccountID);
        transactionDTO.setToAccountNumber(toAccountNumber);
        transactionDTO.setAmount(amount);
        transactionDTO.setIndicator("credit");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
