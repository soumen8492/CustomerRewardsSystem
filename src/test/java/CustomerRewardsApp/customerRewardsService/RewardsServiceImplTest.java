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
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        reward1 = new Reward("1:January", "1", "Alex", "January", 150);
        reward2 = new Reward("2:February", "2", "Leo", "February", 350);
        reward3 = new Reward("3:January", "1", "Alex", "January", 200);
    }

    @Test
    void setRewardPoints_ShouldMapAndSaveRewards() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction1, transaction2));

        rewardsService.setRewardPoints();

        Map<String, HashMap<String, Reward>> rewardMap = new HashMap<>();
        // Adding expected data to the map
        HashMap<String, Reward> monthlyMap1 = new HashMap<>();
        monthlyMap1.put("January", reward1);
        HashMap<String, Reward> monthlyMap2 = new HashMap<>();
        monthlyMap2.put("February", reward2);
        rewardMap.put("1", monthlyMap1);
        rewardMap.put("2", monthlyMap2);

        List<Reward> rewardList = new ArrayList<>();
        rewardList.addAll(monthlyMap1.values());
        rewardList.addAll(monthlyMap2.values());

        rewardsRepository.saveAll(rewardList);
    }

    @Test
    void createRewardResponse_ShouldReturnListOfRewardResponses() {
        List<String> custIdList = Arrays.asList("1", "2");
        when(rewardsRepository.findCustId()).thenReturn(custIdList);
        when(rewardsRepository.findByCustId("1")).thenReturn(Collections.singletonList(reward1));
        when(rewardsRepository.findByCustId("2")).thenReturn(Collections.singletonList(reward2));

        ResponseEntity<List<RewardResponse>> result = rewardsService.createRewardResponse();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("Alex", result.getBody().get(0).getName());
        assertEquals("Leo", result.getBody().get(1).getName());
    }

    @Test
    void createRewardResponseByCustId_ShouldReturnRewardResponse() {
        String custId = "1";
        when(rewardsRepository.findByCustId(custId)).thenReturn(Arrays.asList(reward1, reward3));

        ResponseEntity<RewardResponse> result = rewardsService.createRewardResponse(custId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(custId, result.getBody().getCustId());
        assertEquals("Alex", result.getBody().getName());
        assertEquals(2, result.getBody().getRewardDetails().size());
    }

    @Test
    void createRewardResponseByCustId_ShouldThrowCustomerIdNotFoundException() {
        String custId = "non-existent";
        when(rewardsRepository.findByCustId(custId)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsService.createRewardResponse(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not present in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void computeRewardPoint_ShouldReturnCorrectPoints() {
        assertEquals(0, rewardsService.computeRewardPoint(50));
        assertEquals(25, rewardsService.computeRewardPoint(75));
        assertEquals(50, rewardsService.computeRewardPoint(100));
        assertEquals(100, rewardsService.computeRewardPoint(125));
    }

    // Additional tests for edge cases can be added as needed
}