package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsException.TransactionNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService {
    @Autowired
    RewardsRepository rewardsRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<RewardDTO> calculateRewardPoints() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, HashMap<String, Reward>> rewardMap = new HashMap<>();
        //Mapping and Grouping Rewards by CustId and month of transaction
        for (Transaction t : transactions) {
            HashMap<String, Reward> monthlyMap;
            Reward r;
            if (!rewardMap.containsKey(t.getCustId())) {
                monthlyMap = new HashMap<>();
                monthlyMap.put(t.getTranDate(), new Reward((t.getCustId() + ":" + t.getTranDate()), t.getCustId(), t.getName(), t.getTranDate(), computeRewardPoint(t.getAmount())));
                rewardMap.put(t.getCustId(), monthlyMap);
            } else {
                monthlyMap = rewardMap.get(t.getCustId());
                if (monthlyMap.containsKey(t.getTranDate())) {
                    r = monthlyMap.get(t.getTranDate());
                    r.setPoints(r.getPoints() + computeRewardPoint(t.getAmount()));
                } else {
                    r = new Reward((t.getCustId() + ":" + t.getTranDate()), t.getCustId(), t.getName(), t.getTranDate(), computeRewardPoint(t.getAmount()));
                }
                monthlyMap.put(t.getTranDate(), r);
            }
            rewardMap.put(t.getCustId(), monthlyMap);
        }
        //Converting the group to a Reward List object
        List<Reward> rewardList = new ArrayList<>();
        for (String custId : rewardMap.keySet()) {
            rewardList.addAll(rewardMap.get(custId).values());
        }
        //save the data
        rewardsRepository.saveAll(rewardList);
        //Calculate total for all rewards
        List<RewardDTO> customerTotalReward = new ArrayList<>();
        for (String custId : rewardMap.keySet()) {
            rewardList = rewardsRepository.findByCustId(custId);
            int totalReward = rewardList.stream().mapToInt(Reward::getPoints).sum();
            customerTotalReward.add(new RewardDTO(rewardList, totalReward));
        }
        return customerTotalReward;
    }

    @Override
    public RewardDTO calculateRewardPoints(String custId) {
        List<Reward> rewardList = rewardsRepository.findByCustId(custId);
        int totalReward = rewardList.stream().mapToInt(Reward::getPoints).sum();
        return new RewardDTO(rewardList, totalReward);
    }

    public int computeRewardPoint(int amount) {
        if (amount <= 50) return 0;
        else if (amount <= 100) return (amount - 50);
        else return 2 * (amount - 100) + 50;
    }
}
