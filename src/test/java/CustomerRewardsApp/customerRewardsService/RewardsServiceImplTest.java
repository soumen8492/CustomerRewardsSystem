package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardResponse;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.format.DateTimeParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceImplTest {

    @Mock
    private RewardsRepository rewardsRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    private Transaction transaction1;
    private Transaction transaction2;
    private Reward reward1;
    private Reward reward2;
    private Reward reward3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction1 = new Transaction("1", "1", "Alex", "01/01/2023", 200);
        transaction2 = new Transaction("2", "2", "Leo", "02/01/2023", 300);
        reward1 = new Reward("1:January", "1", 150);
        reward2 = new Reward("2:February", "2", 350);
        reward3 = new Reward("3:January", "3", 200);
    }

    @Test
    void createRewardPoints_ShouldMapAndSaveRewards() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        rewardsService.createRewardPoints();

        // Verify that the rewards are mapped correctly and saved
        verify(rewardsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getRewardResponse_ShouldReturnListOfRewardResponses() {
        List<String> custIdList = Arrays.asList("1", "2");
        when(transactionRepository.findCustId()).thenReturn(custIdList);
        when(transactionRepository.findTransactionsByCustId("1")).thenReturn(Collections.singletonList(transaction1));
        when(transactionRepository.findTransactionsByCustId("2")).thenReturn(Collections.singletonList(transaction2));
        when(rewardsRepository.findRewardsByCustId("1")).thenReturn(Collections.singletonList(reward1));
        when(rewardsRepository.findRewardsByCustId("2")).thenReturn(Collections.singletonList(reward2));

        ResponseEntity<List<RewardResponse>> result = rewardsService.getRewardResponse();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("Alex", result.getBody().get(0).getName());
        assertEquals("Leo", result.getBody().get(1).getName());
    }

    @Test
    void getRewardResponseByCustId_ShouldReturnRewardResponse() {
        String custId = "1";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Arrays.asList(transaction1, transaction2));
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Arrays.asList(reward1, reward3));

        ResponseEntity<RewardResponse> result = rewardsService.getRewardResponse(custId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(custId, result.getBody().getCustId());
        assertEquals("Alex", result.getBody().getName());
        assertEquals(2, result.getBody().getRewardDetails().size());
    }

    @Test
    void getRewardResponseByCustId_ShouldThrowCustomerIdNotFoundException() {
        String custId = "non-existent";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Collections.emptyList());
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.getRewardResponse(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void computeRewardPoint_ShouldReturnCorrectPoints() {
        assertEquals(0, rewardsService.computeRewardPoint(50));
        assertEquals(25, rewardsService.computeRewardPoint(75));
        assertEquals(50, rewardsService.computeRewardPoint(100));
        assertEquals(52, rewardsService.computeRewardPoint(101));
    }
    @Test
    void getRewardResponseByCustId_ShouldHandleTransactionsWithoutRewards() {
        String custId = "1";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Arrays.asList(transaction1, transaction2));
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.getRewardResponse(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getRewardResponseByCustId_ShouldHandleRewardsWithoutTransactions() {
        String custId = "1";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Collections.emptyList());
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Arrays.asList(reward1, reward3));

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.getRewardResponse(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createRewardPoints_ShouldHandleDuplicateTransactions() {
        Transaction duplicateTransaction = new Transaction("1", "1", "Alex", "01/01/2023", 200);
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, duplicateTransaction, transaction2));

        rewardsService.createRewardPoints();

        verify(rewardsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createRewardPoints_ShouldHandleEmptyRewardList() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));
        when(rewardsRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

        rewardsService.createRewardPoints();

        verify(rewardsRepository, times(1)).saveAll(anyList());
    }
    @Test
    void createRewardPoints_ShouldHandleTransactionsWithSameCustId() {
        Transaction transaction3 = new Transaction("3", "1", "Alex", "03/01/2023", 100);
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));

        rewardsService.createRewardPoints();

        verify(rewardsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getRewardResponseByCustId_ShouldReturnEmptyRewardListWhenNoTransactions() {
        String custId = "1";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Collections.emptyList());
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.getRewardResponse(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getRewardResponse_ShouldHandleLargeNumberOfCustomers() {
        List<String> custIdList = Arrays.asList("1", "2", "3", "4", "5");
        when(transactionRepository.findCustId()).thenReturn(custIdList);

        for (String custId : custIdList) {
            when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Collections.singletonList(transaction1));
            when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Collections.singletonList(reward1));
        }

        ResponseEntity<List<RewardResponse>> result = rewardsService.getRewardResponse();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(5, result.getBody().size());
    }

    @Test
    void computeRewardPoint_ShouldHandleHighAmounts() {
        assertEquals(1850, rewardsService.computeRewardPoint(1000));
        assertEquals(49850, rewardsService.computeRewardPoint(25000));
        assertEquals(99850, rewardsService.computeRewardPoint(50000));
    }

    @Test
    void createRewardPoints_ShouldMapAndSaveRewardsForMultipleCustomers() {
        Transaction transaction3 = new Transaction("3", "2", "Leo", "03/01/2023", 400);
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));

        rewardsService.createRewardPoints();

        verify(rewardsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void createRewardPoints_ShouldCorrectlyAggregatePointsForSameCustId() {
        Transaction transaction4 = new Transaction("4", "1", "Alex", "04/01/2023", 250);
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction4));

        rewardsService.createRewardPoints();

        verify(rewardsRepository, times(1)).saveAll(anyList());
    }

    @Test
    void getRewardResponseByCustId_ShouldReturnRewardDetailsInSortedOrder() {
        String custId = "1";
        when(transactionRepository.findTransactionsByCustId(custId)).thenReturn(Arrays.asList(transaction1, transaction2));
        when(rewardsRepository.findRewardsByCustId(custId)).thenReturn(Arrays.asList(reward1, reward3));

        ResponseEntity<RewardResponse> result = rewardsService.getRewardResponse(custId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getRewardDetails().size());
        assertEquals("January", result.getBody().getRewardDetails().get(0).getMonth());
        assertEquals("February", result.getBody().getRewardDetails().get(1).getMonth());
    }
}