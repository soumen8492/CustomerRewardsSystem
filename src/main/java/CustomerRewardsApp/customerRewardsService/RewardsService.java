package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsException.TransactionNotFoundException;
import CustomerRewardsApp.models.Transaction;

import java.util.List;

public interface RewardsService {
    List<Transaction> getAllTransaction();
    List<Transaction> getTransactionByCustId(String custId) throws CustomerIdNotFoundException;
    List<Transaction> getTransactionByCustIdAndMonth(String custId, String month) throws TransactionNotFoundException;
    List<Transaction> updateRewardPoint();
}
