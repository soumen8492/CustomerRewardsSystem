package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.RewardResponse;
import CustomerRewardsApp.models.RewardDetail;
import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class RewardsControllerTest {

    @Mock
    private RewardsServiceImpl rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    private RewardResponse rewardResponse1;
    private RewardResponse rewardResponse2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rewardResponse1 = new RewardResponse();
        rewardResponse1.setCustId("1");
        rewardResponse1.setName("Alex");
        rewardResponse1.setTotal_points(150);
        rewardResponse1.setRewardDetails(Arrays.asList(
                new RewardDetail("January", 100),
                new RewardDetail("February", 50)
        ));

        rewardResponse2 = new RewardResponse();
        rewardResponse2.setCustId("2");
        rewardResponse2.setName("Leo");
        rewardResponse2.setTotal_points(200);
        rewardResponse2.setRewardDetails(Arrays.asList(
                new RewardDetail("January", 150),
                new RewardDetail("February", 50)
        ));
    }

    @Test
    void getTotal_ShouldReturnListOfRewardResponses() {
        List<RewardResponse> rewardResponses = Arrays.asList(rewardResponse1, rewardResponse2);
        when(rewardsService.createRewardResponse()).thenReturn(new ResponseEntity<>(rewardResponses, HttpStatus.OK));

        ResponseEntity<List<RewardResponse>> result = rewardsController.getTotal();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        assertEquals("Alex", result.getBody().get(0).getName());
        assertEquals("Leo", result.getBody().get(1).getName());
    }

    @Test
    void getTotal_ShouldReturnEmptyListWhenNoRewards() {
        when(rewardsService.createRewardResponse()).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        ResponseEntity<List<RewardResponse>> result = rewardsController.getTotal();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
    }

    @Test
    void getTotalByCustId_ShouldReturnRewardResponseForGivenCustomerId() {
        String custId = "1";
        when(rewardsService.createRewardResponse(custId)).thenReturn(new ResponseEntity<>(rewardResponse1, HttpStatus.OK));

        ResponseEntity<RewardResponse> result = rewardsController.getTotalByCustId(custId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(custId, result.getBody().getCustId());
        assertEquals("Alex", result.getBody().getName());
    }

    @Test
    void getTotalByCustId_ShouldReturnNotFoundForNonExistentCustomerId() {
        String custId = "non-existent";
        doThrow(new CustomerIdNotFoundException("Customer Id " + custId + " not present in DB", "Id does not exist"))
                .when(rewardsService).createRewardResponse(custId);

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsController.getTotalByCustId(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not present in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getTotalByCustId_ShouldReturnEmptyResponseForNullCustomerId() {
        String custId = null;
        doThrow(new CustomerIdNotFoundException("Customer Id " + custId + " not present in DB", "Id does not exist"))
                .when(rewardsService).createRewardResponse(custId);

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsController.getTotalByCustId(custId);
        });

        String expectedMessage = "Customer Id " + custId + " not present in DB";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Additional tests for other edge cases can be added as needed
}
