package CustomerRewardsApp.customerRewardsRepository;

import CustomerRewardsApp.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardsRepository extends JpaRepository<Transaction, String> {
    @Query(value = "SELECT * from CUSTOMERTRANSACTION where cust_id= :custId", nativeQuery = true)
    List<Transaction> getTransactionByCustId(@Param("custId") String custId);
    @Query(value = "SELECT * from CUSTOMERTRANSACTION where cust_id= :custId and mnth= :month", nativeQuery = true)
    List<Transaction> getTransactionByCustIdAndMonth(@Param("custId") String custId, @Param("month") String month);
}
