package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {
    @Mock
    private RewardsServiceImpl rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    private RewardDTO rewardDTO1;
    private RewardDTO rewardDTO2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rewardDTO1 = new RewardDTO();
        rewardDTO1.setTotal_points(100);
        rewardDTO2 = new RewardDTO();
        rewardDTO2.setTotal_points(200);
    }

    @Test
    void getTotal_ShouldReturnListOfRewardDTOs() {
        List<RewardDTO> rewardDTOList = Arrays.asList(rewardDTO1, rewardDTO2);
        when(rewardsService.calculateRewardPoints()).thenReturn(rewardDTOList);

        List<RewardDTO> result = rewardsController.getTotal();

        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getTotal_points());
        assertEquals(200, result.get(1).getTotal_points());
    }

    @Test
    void getTotalByCustId_ShouldReturnNotExistentCustomerId() {
        String custId = "non-existent";
        doThrow(new CustomerIdNotFoundException("CustId not present", "CustId " + custId + " not present in DB"))
                .when(rewardsService).calculateRewardPoints(custId);

        Exception exception = assertThrows(CustomerIdNotFoundException.class, () -> {
            rewardsController.getTotalByCustId(custId);
        });

        String expectedMessage = "CustId not present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}