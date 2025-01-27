package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsException.TransactionNotFoundException;
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
        if(this.getTransactionByCustId(custId).isEmpty()) throw new CustomerIdNotFoundException("Customer Id :"+custId+" not in db", "CustId not present");
        List<Transaction> trns = this.getTransactionByCustId(custId);
        int reward = trns.stream().mapToInt(Transaction::getPoints).sum();
        return new Reward(custId, reward);
    }
    public Reward getTotalReward(String custId, String month)
    {
        List<Transaction> trns;
        if(this.getTransactionByCustIdAndMonth(custId, month).isEmpty()) throw new CustomerIdNotFoundException("Customer Id :"+custId+" On the month of "+month+" not in db", "CustId not present");
        else trns = this.getTransactionByCustIdAndMonth(custId, month);
        int reward = trns.stream().mapToInt(Transaction::getPoints).sum();
        return new Reward(custId, month, reward);
    }

    public List<Reward> getTotalReward() {
        List<Transaction> trn = this.getAllTransaction();
        Map<String, Integer> custMap = new HashMap<>();

        //Map to contain all sum values
        trn.forEach(t -> custMap.merge(t.getCustId(), t.getPoints(), Integer::sum));

        //Output list
        ArrayList<Reward> rewards = new ArrayList<>();
        custMap.forEach( (a, b) -> rewards.add(new Reward(a,b)));
        return rewards;
    }
}
