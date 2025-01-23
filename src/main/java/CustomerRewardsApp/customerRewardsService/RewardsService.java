package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.models.Transaction;

import java.util.List;

public interface RewardsService {
    List<Transaction> getAllTransaction();
    List<Transaction> getTransactionByCustId(String custId);
    List<Transaction> getTransactionByCustIdAndMonth(String custId, String month);
    List<Transaction> updateRewardPoint();
}
