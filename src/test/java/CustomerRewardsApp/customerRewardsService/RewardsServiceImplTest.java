package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RewardsServiceImplTest {

    @InjectMocks
    RewardsServiceImpl rewardsService;

    @Mock
    RewardsRepository rewardsRepository;

    @Test
    void getAllTransaction() {
        when(rewardsRepository.findAll()).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan"))
                        .collect(Collectors.toList()));
        assertEquals(1, rewardsService.getAllTransaction().size());
    }

    @Test
    void getTransactionByCustId() {
        when(rewardsRepository.getTransactionByCustId("1")).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan"),
                        new Transaction("2","1",50,"feb")
                        ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTransactionByCustId("1").size());
    }

    @Test
    void getTransactionByCustIdAndMonth() {
        when(rewardsRepository.getTransactionByCustIdAndMonth("1", "jan")).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan"),
                        new Transaction("2","1",50,"jan")
                ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTransactionByCustIdAndMonth("1","jan").size());
    }

    @Test
    void updateRewardPoint() {
        when(rewardsService.getAllTransaction()).thenReturn(
                Stream.of(new Transaction("1","1",125,"jan"),
                        new Transaction("2","1",75,"jan")
                ).collect(Collectors.toList()));
        assertEquals(0, rewardsService.updateRewardPoints().size());
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
                new Transaction("1","1",150,"jan"),
                new Transaction("2","1",75,"feb")
        ).collect(Collectors.toList()));
        assertEquals(175, rewardsService.getTotalReward("1").getTotalRewards());
    }

    @Test
    void testGetTotalRewardAll() {
        when(rewardsRepository.findAll()).thenReturn(Stream.of(
                new Transaction("1","1",170,"jan"),
                new Transaction("2","2",75,"jan"),
                new Transaction("3","2",25,"feb")
        ).collect(Collectors.toList()));
        assertEquals(2, rewardsService.getTotalReward().size());
    }
}