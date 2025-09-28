package org.digitalwallet.app.service;

import org.digitalwallet.app.dto.WalletDTO;
import org.digitalwallet.app.dto.request.DepositRequest;
import org.digitalwallet.app.dto.request.WalletFilterRequest;
import org.digitalwallet.app.dto.request.WithdrawRequest;
import org.digitalwallet.app.enumeration.Currency;
import org.digitalwallet.app.enumeration.Role;
import org.digitalwallet.app.exception.WalletNotActiveWithdrawException;
import org.digitalwallet.app.exception.WalletNotFoundException;
import org.digitalwallet.app.mapper.WalletMapper;
import org.digitalwallet.app.repository.WalletRepository;
import org.digitalwallet.app.repository.model.UserAuth;
import org.digitalwallet.app.repository.model.Wallet;
import org.digitalwallet.app.util.CustomUserDetails;
import org.digitalwallet.app.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private WalletMapper walletMapper;

    @Mock
    private TransactionService transactionService;

    @Mock
    private UserAuthService userAuthService;

    @InjectMocks
    private WalletService walletService;

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
    void testFindById_Success() {
        var expected = Optional.of(TestUtil.createWallet(1L));
        when(walletRepository.findById(any())).thenReturn(expected);
        Wallet actual = walletService.findById(1L);
        assertEquals(actual, expected.get());
    }

    @Test
    void testFindById_WalletNotFound_Failure() {
        when(walletRepository.findById(any())).thenThrow(new WalletNotFoundException("Wallet not found"));
        assertThrows(WalletNotFoundException.class, () -> walletService.findById(1L));
    }

    @Test
    void testCreateWallet_Success() {
        var createWalletRequest = TestUtil.createWalletRequest();
        when(walletMapper.toEntity(createWalletRequest)).thenReturn(TestUtil.createWallet(1L));
        walletService.createWallet(createWalletRequest, auth);
        verify(walletRepository, times(1)).save(any());
    }

    @Test
    void testListWallets_Success() {
        List<Wallet> wallets = List.of(TestUtil.createWallet(1L));
        List<WalletDTO> walletDtoList = List.of(TestUtil.createWalletDTO(1L));
        WalletFilterRequest filter = new WalletFilterRequest();
        filter.setCurrency(Currency.EUR.toString());
        filter.setWalletName("TEST");
        when(walletMapper.toDtoList(anyList())).thenReturn(walletDtoList);
        when(walletRepository.findByCustomerIdAndCurrencyAndWalletName(anyLong(), anyString(), anyString()))
                .thenReturn(wallets);
        List<WalletDTO> result = walletService.listWallets(1L, filter, auth);
        assertEquals(walletDtoList.size(), result.size());
        assertEquals(walletDtoList.getFirst().getCustomerId(), result.getFirst().getCustomerId());
        assertEquals(walletDtoList.getFirst().getCurrency(), result.getFirst().getCurrency());
        assertEquals(walletDtoList.getFirst().getWalletName(), result.getFirst().getWalletName());
        verify(walletMapper).toDtoList(wallets);
    }

    @Test
    void testDeposit_WalletNotFound_Failure() {
        when(walletRepository.findById(any())).thenThrow(new WalletNotFoundException("Wallet not found"));
        assertThrows(WalletNotFoundException.class, () -> walletService.deposit(1L, TestUtil.createDepositRequest(1000d), auth));
    }

    @Test
    void testDeposit_AmountOver1000_Successful() {
        Long walletId = 2L;
        var request = TestUtil.createDepositRequest(1500d);
        var wallet = TestUtil.createWallet(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.deposit(walletId, request, auth);
        verify(transactionService).createTransaction(anyLong(), anyDouble(), anyString(), any(), anyBoolean());
    }

    @Test
    void testDeposit_AmountBelow1000_Successful() {
        Long walletId = 3L;
        DepositRequest request = TestUtil.createDepositRequest(1500d);
        Wallet wallet = TestUtil.createWallet(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.deposit(walletId, request, auth);
        verify(transactionService, times(1)).createTransaction(anyLong(), anyDouble(), anyString(), any(), anyBoolean());
    }

    @Test
    void testWithdraw_WalletNotFound_Failure() {
        when(walletRepository.findById(any())).thenThrow(new WalletNotFoundException("Wallet not found"));
        assertThrows(WalletNotFoundException.class,
                () -> walletService.withdraw(1L, TestUtil.createWithdrawRequest(1000d), auth));
    }

    @Test
    void testWithdraw_amountOver1000_Successful() {
        Long walletId = 4L;
        WithdrawRequest request = TestUtil.createWithdrawRequest(1500d);
        Wallet wallet = TestUtil.createWallet(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.withdraw(walletId, request, auth);
        verify(transactionService, times(1)).createTransaction(anyLong(), anyDouble(), anyString(), any(), anyBoolean());
    }

    @Test
    void testWithdraw_amountBelow1000_Successful() {
        Long walletId = 5L;
        WithdrawRequest request = TestUtil.createWithdrawRequest(1500d);
        Wallet wallet = TestUtil.createWallet(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        walletService.withdraw(walletId, request, auth);
        verify(transactionService, times(1)).createTransaction(anyLong(), anyDouble(), anyString(), any(), anyBoolean());
    }

    @Test
    void testWithdraw_walletWithdrawNotAllowed_Failure() {
        Long walletId = 6L;
        WithdrawRequest request = TestUtil.createWithdrawRequest(500d);
        Wallet wallet = TestUtil.createWallet(walletId);
        wallet.setActiveForWithdraw(false);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        assertThrows(WalletNotActiveWithdrawException.class,
                () -> walletService.withdraw(walletId, request, auth));
    }
}