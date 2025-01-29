package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            String month = LocalDate.parse(t.getTranDate(), DateTimeFormatter.ofPattern("MM/dd/yyyy")).format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
            Reward r;
            if (!rewardMap.containsKey(t.getCustId())) {
                monthlyMap = new HashMap<>();
                monthlyMap.put(month, new Reward((t.getCustId() + ":" + month), t.getCustId(), t.getName(), month, computeRewardPoint(t.getAmount())));
                rewardMap.put(t.getCustId(), monthlyMap);
            } else {
                monthlyMap = rewardMap.get(t.getCustId());
                if (monthlyMap.containsKey(month)) {
                    r = monthlyMap.get(month);
                    r.setPoints(r.getPoints() + computeRewardPoint(t.getAmount()));
                } else {
                    r = new Reward((t.getCustId() + ":" + month), t.getCustId(), t.getName(), month, computeRewardPoint(t.getAmount()));
                }
                monthlyMap.put(month, r);
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
        if(rewardList.isEmpty()) throw new CustomerIdNotFoundException("CustId not present", "CustId "+custId+" not present in DB");
        int totalReward = rewardList.stream().mapToInt(Reward::getPoints).sum();
        return new RewardDTO(rewardList, totalReward);
    }

    public int computeRewardPoint(int amount) {
        if (amount <= 50) return 0;
        else if (amount <= 100) return (amount - 50);
        else return 2 * (amount - 100) + 50;
    }
}
