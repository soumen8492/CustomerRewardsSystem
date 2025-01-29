package CustomerRewardsApp.customerRewardsRepository;

import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class RewardsRepositoryTest {

    @Mock
    private RewardsRepository rewardsRepository;

    private Reward reward1;
    private Reward reward2;
    private Reward reward3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reward1 = new Reward("1:January", "1", "Alex", "January", 100);
        reward2 = new Reward("2:February", "2", "Leo", "February", 200);
        reward3 = new Reward("3:March", "1", "Alex", "March", 300);
    }

    @Test
    void testFindByCustId_ShouldReturnRewardsForGivenCustId() {
        when(rewardsRepository.findByCustId("1")).thenReturn(Arrays.asList(reward1, reward3));

        List<Reward> rewards = rewardsRepository.findByCustId("1");

        assertEquals(2, rewards.size());
        assertTrue(rewards.stream().anyMatch(reward -> reward.getPoints() == 100));
        assertTrue(rewards.stream().anyMatch(reward -> reward.getPoints() == 300));
    }

    @Test
    void testFindByCustId_ShouldReturnEmptyListForNonExistentCustId() {
        when(rewardsRepository.findByCustId("non-existent")).thenReturn(Arrays.asList());

        List<Reward> rewards = rewardsRepository.findByCustId("non-existent");

        assertTrue(rewards.isEmpty());
    }

    @Test
    void testFindByCustId_ShouldReturnEmptyListForNullCustId() {
        when(rewardsRepository.findByCustId(null)).thenReturn(Arrays.asList());

        List<Reward> rewards = rewardsRepository.findByCustId(null);

        assertTrue(rewards.isEmpty());
    }
}