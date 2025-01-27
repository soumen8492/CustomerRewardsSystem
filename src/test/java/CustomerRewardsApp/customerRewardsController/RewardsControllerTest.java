package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RewardsControllerTest {
    @Autowired
    RewardsController rewardsController;
    @Autowired
    MockMvc mockmvc;
    @MockitoBean
    RewardsServiceImpl rewardsService;

    @Test
    void getAllTransactions() throws Exception {
        when(rewardsService.getAllTransaction()).thenReturn(Stream.of(
                new Transaction("1","1",100,"jan",0),
                new Transaction("2","3",150,"mar",0)
                ).collect(Collectors.toList()));
        MvcResult result = mockmvc.perform(get("/rewards/allTransaction")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertEquals(2, str.split("},").length);
    }

    @Test
    void calculateRewardPoint() throws Exception {
        when(rewardsService.updateRewardPoints()).thenReturn(Stream.of(
                new Transaction("1","1",100,"jan",50),
                new Transaction("2","3",150,"mar",150)
        ).collect(Collectors.toList()));
        MvcResult result = mockmvc.perform(patch("/rewards/calcRewards")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertTrue(str.contains("points\":50") && str.contains("points\":150"));
    }

    @Test
    void getByCustIdAndMonth() throws Exception {
        when(rewardsService.getTransactionByCustIdAndMonth("1","jan")).thenReturn(Stream.of(
                new Transaction("1","1",100,"jan",0),
                new Transaction("2","1",150,"jan",0)
        ).collect(Collectors.toList()));
        MvcResult result = mockmvc.perform(get("/rewards/transactions/1/jan")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertEquals(2, str.split("},").length);
    }

    @Test
    void getByCustId() throws Exception{
        when(rewardsService.getTransactionByCustId("1")).thenReturn(Stream.of(
                new Transaction("1","1",100,"jan",0),
                new Transaction("2","1",150,"jan",0)
        ).collect(Collectors.toList()));
        MvcResult result = mockmvc.perform(get("/rewards/transactions/1")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertEquals(2, str.split("},").length);
    }

    @Test
    void getTotal() throws Exception{
        when(rewardsService.getTotalReward()).thenReturn(Stream.of(
                new Reward("1"),
                new Reward("1")
        ).collect(Collectors.toList()));
        MvcResult result = mockmvc.perform(get("/rewards/total")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertEquals(2, str.split("},\\{").length);
    }

    @Test
    void getTotalByCustId() throws Exception{
        when(rewardsService.getTotalReward("1")).thenReturn(
                new Reward("1")
        );
        MvcResult result = mockmvc.perform(get("/rewards/total/1")).andExpect(status().isOk()).andReturn();
        String str = result.getResponse().getContentAsString();
        assertTrue(str.contains("totalRewards\":0"));
    }
}