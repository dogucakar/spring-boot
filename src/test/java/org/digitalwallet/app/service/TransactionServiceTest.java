package org.digitalwallet.app.service;

import org.digitalwallet.app.enumeration.TransactionStatus;
import org.digitalwallet.app.enumeration.TransactionType;
import org.digitalwallet.app.exception.TransactionNotFoundException;
import org.digitalwallet.app.repository.TransactionRepository;
import org.digitalwallet.app.repository.model.Transaction;
import org.digitalwallet.app.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
	@Mock
	private TransactionRepository transactionRepository;
	@InjectMocks
	private TransactionService transactionService;

	@Test
	void save_Success() {
		transactionService.save(TestUtil.createTransaction(TransactionType.DEPOSIT, TransactionStatus.APPROVED));
		verify(transactionRepository, times(1)).save(any());
	}

	@Test
	void findById_Success() {
		Transaction transaction = new Transaction();
		when(transactionRepository.findById(any())).thenReturn(Optional.of(transaction));
		assertNotNull(transactionService.findById(1L));
		verify(transactionRepository, times(1)).findById(any());
	}

	@Test
	void findById_TransactionNotFound_Failure() {
		when(transactionRepository.findById(any())).thenThrow(new TransactionNotFoundException("Transaction not found"));
		assertThrows(TransactionNotFoundException.class, () -> {transactionService.findById(1L);
		});
	}

	@ParameterizedTest
	@MethodSource("sourceTransactionValues")
	void testCreateTransaction_Success(TransactionType transactionType, boolean isPending) {
		transactionService.createTransaction(1L, 1000d, "TR12345", transactionType, isPending);
		verify(transactionRepository, times(1)).save(any());
	}

	static Stream<Arguments> sourceTransactionValues() {
		return Stream.of(
				Arguments.of(TransactionType.DEPOSIT, true),
				Arguments.of(TransactionType.DEPOSIT, false),
				Arguments.of(TransactionType.WITHDRAW, true),
				Arguments.of(TransactionType.WITHDRAW, false)
		);
	}

	@Test
	void testListTransactions_Success() {
		Long walletId = 1L;
		when(transactionRepository.findByWalletId(any())).
				thenReturn(List.of(TestUtil.createTransaction(TransactionType.DEPOSIT, TransactionStatus.APPROVED)));
		transactionService.listTransactions(walletId);
		verify(transactionRepository, times(1)).findByWalletId(any());
	}
}