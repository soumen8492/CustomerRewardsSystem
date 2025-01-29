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
    List<Reward> findByCustId(String custId);
    @Query(value = "select distinct cust_id from REWARDPOINTS order by cust_id asc", nativeQuery = true)
    List<String> findCustId();
}
