package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsException.TransactionNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService{
    @Autowired
    RewardsRepository rewardsRepository;
    @Override
    public List<Transaction> getAllTransaction() {
        return rewardsRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionByCustId(String custId) {
        List<Transaction> transactions = rewardsRepository.getTransactionByCustId(custId);;
        if(transactions.isEmpty()) {
            throw new CustomerIdNotFoundException(("CustId : "+custId+" is not present in the Database"),"CustId not found");
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionByCustIdAndMonth(String custId, String month) {
        List<Transaction> transactions = rewardsRepository.getTransactionByCustIdAndMonth(custId,month);
        if(transactions.isEmpty()) {
            throw new TransactionNotFoundException(("CustId : "+custId+" in the month "+month+" is not present in the Database"),"TransactionResponseEntity<?> not found");
        }
        return transactions;
    }

    @Override
    public List<Transaction> updateRewardPoints() {
        List<Transaction> transactions = this.getAllTransaction();
        transactions.forEach(t -> t.setPoints(calculateRewardPoint(t)));
        return rewardsRepository.saveAll(transactions);
    }
    public int calculateRewardPoint(Transaction trn)
    {
        int amount = trn.getAmount();
        if(amount<=50) return 0;
        else if(amount<=100) return (amount-50);
        else return 2*(amount-100)+50;
    }
    public Reward getTotalReward(String custId)
    {
        List<Transaction> trns = this.getTransactionByCustId(custId);
        Reward reward = new Reward();
        reward.setCustId(custId);
        if(trns.isEmpty()) throw new CustomerIdNotFoundException("Customer Id :"+custId+" not in db", "CustId not present");

        //Logic to get month wise rewards
        trns.forEach(t -> reward.getMonthlyRewards().merge(t.getMonthOfTransaction(),t.getPoints(),Integer::sum));
        reward.setTotalRewards(reward.getMonthlyRewards().values().stream().mapToInt(i->i).sum());
        return reward;
    }

    public List<Reward> getTotalReward() {
        List<Transaction> trn = this.getAllTransaction();
        Map<String, Reward> rewardMap = new HashMap<>();

        //Stream api logic to create rewardMap with custId
        trn.stream().forEach(t -> {
            rewardMap.computeIfAbsent(t.getCustId(), k -> new Reward(t.getCustId()))
                    .getMonthlyRewards()
                    .merge(t.getMonthOfTransaction(), t.getPoints(), Integer::sum);
        });
        rewardMap.values().forEach(r -> r.setTotalRewards(r.getMonthlyRewards().values().stream().mapToInt(i->i).sum()));
        //Output list
        List<Reward> rewards = rewardMap.values().stream().collect(Collectors.toList());
        return rewards;
    }
}
