package CustomerRewardsApp.customerRewardsRepository;

import CustomerRewardsApp.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query(value = "select distinct cust_id from CUSTOMERTRANSACTION order by cust_id asc", nativeQuery = true)
    List<String> findCustId();

    @Query(value = "select * from CUSTOMERTRANSACTION WHERE cust_id =:custId ORDER BY tran_id", nativeQuery = true)
    List<Transaction> findTransactionsByCustId(@Param("custId") String custId);
}
