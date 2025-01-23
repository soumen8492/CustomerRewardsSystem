package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardsServiceImpl implements RewardsService{
    @Autowired
    RewardsRepository rewardsRepository;
    @Override
    public List<Transaction> getAllTransaction() {
        return (List<Transaction>) rewardsRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionByCustId(String custId) {
        return rewardsRepository.getTransactionByCustId(custId);
    }

    @Override
    public List<Transaction> getTransactionByCustIdAndMonth(String custId, String month) {
        return rewardsRepository.getTransactionByCustIdAndMonth(custId, month);
    }

    @Override
    public List<Transaction> updateRewardPoint() {
        List<Transaction> transactions = this.getAllTransaction();
        for (Transaction t:
             transactions) {
            int point = calculateRewardPoint(t);
            t.setPoints(point);
        }
        return rewardsRepository.saveAll(transactions);
    }
    public int calculateRewardPoint(Transaction trn)
    {
        int amount = trn.getAmount();
        if(amount<50) return 0;
        else if(amount>50 && amount<100) return (amount-50);
        else return 2*(amount-100)+50;
    }
    public Reward getTotalReward(String custId)
    {
        List<Transaction> trns = this.getTransactionByCustId(custId);
        int reward = trns.stream().mapToInt(Transaction::getPoints).sum();
        return new Reward(custId, reward);
    }
    public Reward getTotalReward(String custId, String month)
    {
        List<Transaction> trns = this.getTransactionByCustIdAndMonth(custId, month);
        int reward = trns.stream().mapToInt(Transaction::getPoints).sum();
        return new Reward(custId, month, reward);
    }

    public List<Reward> getTotalReward() {
        List<Transaction> trn = this.getAllTransaction();
        Map<String, Integer> custMap = new HashMap<>();
        for (Transaction t:
             trn) {
            if(custMap.containsKey(t.getCustId())) custMap.put(t.getCustId(), custMap.get(t.getCustId())+t.getPoints());
            else custMap.put(t.getCustId(),t.getPoints());
        }
        ArrayList<Reward> rewards = new ArrayList<>();
        for(String s : custMap.keySet())
        {
            rewards.add(new Reward(s,custMap.get(s)));
        }
        return rewards;
    }
}
