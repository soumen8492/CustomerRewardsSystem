package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsServiceImplTest {
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction1 = new Transaction("1", "1", "Alex", "01/01/2023", 200);
        transaction2 = new Transaction("2", "2", "Leo", "02/01/2023", 300);
        reward1 = new Reward("1:January", "1", "Alex", "January", 150);
        reward2 = new Reward("2:February", "2", "Leo", "February", 350);
    }

    @Test
    void calculateRewardPoints_ShouldReturnRewardDTOList() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));
        when(rewardsRepository.findByCustId("1")).thenReturn(Collections.singletonList(reward1));
        when(rewardsRepository.findByCustId("2")).thenReturn(Collections.singletonList(reward2));

        List<RewardDTO> result = rewardsService.calculateRewardPoints();

        assertEquals(2, result.size());
        assertEquals(150, result.get(0).getTotal_points());
        assertEquals(350, result.get(1).getTotal_points());
    }

    @Test
    void calculateRewardPoints_ShouldThrowCustomerIdNotFoundException() {
        String custId = "non-existent";
        when(rewardsRepository.findByCustId(custId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.calculateRewardPoints(custId);
        });

        String expectedMessage = "CustId not present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void calculateRewardPointsByCustId_ShouldReturnRewardDTO() {
        String custId = "1";
        when(rewardsRepository.findByCustId(custId)).thenReturn(Collections.singletonList(reward1));

        RewardDTO result = rewardsService.calculateRewardPoints(custId);

        assertNotNull(result);
        assertEquals(150, result.getTotal_points());
    }

    @Test
    void computeRewardPoint_ShouldReturnCorrectPoints() {
        assertEquals(0, rewardsService.computeRewardPoint(50));
        assertEquals(25, rewardsService.computeRewardPoint(75));
        assertEquals(50, rewardsService.computeRewardPoint(100));
        assertEquals(100, rewardsService.computeRewardPoint(125));
    }
}