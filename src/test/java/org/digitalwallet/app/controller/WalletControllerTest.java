
package org.digitalwallet.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digitalwallet.app.dto.request.DepositRequest;
import org.digitalwallet.app.dto.request.TransactionOperationRequest;
import org.digitalwallet.app.dto.request.WalletRequest;
import org.digitalwallet.app.dto.request.WithdrawRequest;
import org.digitalwallet.app.enumeration.Currency;
import org.digitalwallet.app.enumeration.Role;
import org.digitalwallet.app.repository.model.UserAuth;
import org.digitalwallet.app.service.TransactionService;
import org.digitalwallet.app.service.WalletService;
import org.digitalwallet.app.util.CustomUserDetails;
import org.digitalwallet.app.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
@Import(WalletControllerTest.Config.class)
@AutoConfigureMockMvc(addFilters = false)
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private static Authentication auth;

    @BeforeAll
    public static void testWithCustomUserDetails() {
        UserAuth testUserAuth = new UserAuth();
        testUserAuth.setId(1L);
        testUserAuth.setUsername("john");
        testUserAuth.setPassword("pass");
        testUserAuth.setRole(Role.EMPLOYEE);
        CustomUserDetails userDetails = new CustomUserDetails(testUserAuth);
        auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void testCreateWallet_InvalidCurrency_Failure() throws Exception {
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setCurrency("ABC");
        mockMvc.perform(post("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreateWallet_Successful() throws Exception {
        WalletRequest walletRequest = new WalletRequest();
        mockMvc.perform(post("/api/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletRequest)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).createWallet(any(), any());
    }

    @Test
    void testListWallets_Successful() throws Exception {
        when(walletService.listWallets(eq(1L), any(), any())).thenReturn(List.of(TestUtil.createWalletDTO(1L)));
        mockMvc.perform(get("/api/wallet/1")
                        .param("customerId", "1")
                        .param("currency", Currency.EUR.toString())
                        .param("walletName", "TEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(walletService, times(1)).listWallets(anyLong(), any(), any());
    }

    @Test
    void testDeposit_Successful() throws Exception {
        DepositRequest request = new DepositRequest();
        mockMvc.perform(post("/api/wallet/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).deposit(anyLong(), any(), any());
    }

    @Test
    void testListTransactions_Successful() throws Exception {
        mockMvc.perform(get("/api/wallet/1/transactions"))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).listTransactions(anyLong());
    }

    @Test
    void testWithdraw_Successful() throws Exception {
        WithdrawRequest request = new WithdrawRequest();
        mockMvc.perform(post("/api/wallet/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).withdraw(anyLong(), any(), any());
    }

    @Test
    void testProgressTransaction_Successful() throws Exception {
        TransactionOperationRequest request = new TransactionOperationRequest();
        mockMvc.perform(post("/api/wallet/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
        verify(walletService, times(1)).progressTransaction(any(), any());
    }

    @TestConfiguration
    static class Config{
        @Bean
        public WalletService walletService() {
            return Mockito.mock(WalletService.class);
        }

        @Bean
        public TransactionService transactionService() {
            return Mockito.mock(TransactionService.class);
        }
    }
}
