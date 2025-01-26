package CustomerRewardsApp.customerRewardsRepository;

import CustomerRewardsApp.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class RewardsRepositoryTest {

    @Autowired
    RewardsRepository rewardsRepository;

    @Test
    void getTransactionByCustId() {
        assertEquals(18,rewardsRepository.getTransactionByCustId("1").size());
    }

    @Test
    void getTransactionByCustIdAndMonth() {
        assertEquals(8,rewardsRepository.getTransactionByCustIdAndMonth("1","jan").size());
    }
}