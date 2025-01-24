package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsServiceImplTest {

    @Autowired
    RewardsServiceImpl rewardsService;

    @MockitoBean
    RewardsRepository rewardsRepository;

    @Test
    void getAllTransaction() {
        when(rewardsRepository.findAll()).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan",0))
                        .collect(Collectors.toList()));
        assertEquals(1, rewardsService.getAllTransaction().size());
    }

    @Test
    void getTransactionByCustId() {
        when(rewardsRepository.getTransactionByCustId("1")).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan",0),
                        new Transaction("2","1",50,"feb",0)
                        ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTransactionByCustId("1").size());
    }

    @Test
    void getTransactionByCustIdAndMonth() {
        when(rewardsRepository.getTransactionByCustIdAndMonth("1", "jan")).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan",0),
                        new Transaction("2","1",50,"jan",0)
                ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTransactionByCustIdAndMonth("1","jan").size());
    }

    @Test
    void updateRewardPoint() {
        when(rewardsService.getAllTransaction()).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan",0),
                        new Transaction("2","1",75,"jan",0)
                ).collect(Collectors.toList()));
        //System.out.println(rewardsService.updateRewardPoint().size());
        assertEquals(2, rewardsService.updateRewardPoint().size());
    }

    @Test
    void calculateRewardPointGreaterThan100() {
        Transaction trn = new Transaction();
        trn.setTransactionId("1");
        trn.setCustId("1");
        trn.setPoints(0);
        trn.setAmount(125);
        trn.setMonthOfTransaction("jan");
        assertEquals(100, rewardsService.calculateRewardPoint(trn));
    }
    @Test
    void calculateRewardPointBetween50And100() {
        Transaction trn = new Transaction();
        trn.setTransactionId("1");
        trn.setCustId("1");
        trn.setPoints(0);
        trn.setAmount(75);
        trn.setMonthOfTransaction("jan");
        assertEquals(25, rewardsService.calculateRewardPoint(trn));
    }
    @Test
    void calculateRewardPointlessThan50() {
        Transaction trn = new Transaction();
        trn.setTransactionId("1");
        trn.setCustId("1");
        trn.setPoints(0);
        trn.setAmount(25);
        trn.setMonthOfTransaction("jan");
        assertEquals(0, rewardsService.calculateRewardPoint(trn));
    }

    @Test
    void getTotalRewardWithCustId() {
        when(rewardsRepository.getTransactionByCustId("1")).thenReturn(Stream.of(
                new Transaction("1","1",150,"jan",150),
                new Transaction("2","1",75,"feb",25)
        ).collect(Collectors.toList()));
        assertEquals(175, rewardsService.getTotalReward("1").getPoint());
    }

    @Test
    void testGetTotalRewardWithCustIdAndMonth() {
        when(rewardsRepository.getTransactionByCustIdAndMonth("1","jan")).thenReturn(Stream.of(
                new Transaction("1","1",170,"jan",190),
                new Transaction("2","1",75,"jan",25)
        ).collect(Collectors.toList()));
        assertEquals(215, rewardsService.getTotalReward("1","jan").getPoint());
    }

    @Test
    void testGetTotalRewardAll() {
        when(rewardsRepository.findAll()).thenReturn(Stream.of(
                new Transaction("1","1",170,"jan",190),
                new Transaction("2","2",75,"jan",25),
                new Transaction("3","2",25,"feb",0)
        ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTotalReward().size());
    }
}