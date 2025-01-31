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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsServiceImpl rewardsService;

    private RewardResponse rewardResponse1;
    private RewardResponse rewardResponse2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rewardResponse1 = new RewardResponse();
        rewardResponse1.setCustId("1");
        rewardResponse1.setName("Alex");
        rewardResponse1.setTotal_points(250);

        rewardResponse2 = new RewardResponse();
        rewardResponse2.setCustId("2");
        rewardResponse2.setName("Leo");
        rewardResponse2.setTotal_points(300);
    }

    @Test
    void getTotal_ShouldReturnListOfRewardResponses() throws Exception {
        List<RewardResponse> rewardResponseList = Arrays.asList(rewardResponse1, rewardResponse2);
        when(rewardsService.getRewardResponse()).thenReturn(new ResponseEntity<>(rewardResponseList, HttpStatus.OK));

        mockMvc.perform(get("/rewards/total"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("\"Alex\"")));
    }

    @Test
    void getTotalByCustId_ShouldReturnRewardResponse() throws Exception {
        when(rewardsService.getRewardResponse(anyString())).thenReturn(new ResponseEntity<>(rewardResponse1, HttpStatus.OK));

        mockMvc.perform(get("/rewards/total/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.custId").value("1"))
                .andExpect(jsonPath("$.name").value("Alex"))
                .andExpect(jsonPath("$.total_points").value(250));
    }

    @Test
    void getTotalByCustId_ShouldReturnNotFound() throws Exception {
        when(rewardsService.getRewardResponse(anyString())).thenThrow(new CustomerIdNotFoundException("Customer Id not in DB", "Customer ID does not exist"));

        mockMvc.perform(get("/rewards/total/non-existent"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("Customer ID non-existent does not exist"));
    }

}