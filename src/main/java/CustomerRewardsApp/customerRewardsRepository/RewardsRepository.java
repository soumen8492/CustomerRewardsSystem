package CustomerRewardsApp.customerRewardsRepository;

import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardsRepository extends JpaRepository<Reward, String> {
    @Query(value = "SELECT rp.* " +
            "FROM REWARDPOINTS rp " +
            "JOIN CUSTOMERTRANSACTION ct ON rp.transaction_id = ct.tran_id " +
            "WHERE ct.cust_id =:custId ORDER BY tran_id",nativeQuery = true)
    List<Reward> findRewardsByCustId(@Param("custId") String custId);
}
