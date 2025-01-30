package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDetail;
import CustomerRewardsApp.models.RewardResponse;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public void setRewardPoints() {
        List<Transaction> transactions = transactionRepository.findAll();
        Map<String, HashMap<String, Reward>> rewardMap = new HashMap<>();
        //Mapping and Grouping Rewards by CustId and month of transaction
        for (Transaction t : transactions) {
            HashMap<String, Reward> monthlyMap;
            String month = LocalDate.parse(t.getTranDate(), DateTimeFormatter.
                            ofPattern("MM/dd/yyyy")).
                    format(DateTimeFormatter.
                            ofPattern("MMMM", Locale.ENGLISH));
            Reward r;
            if (!rewardMap.containsKey(t.getCustId())) {
                monthlyMap = new HashMap<>();
                monthlyMap.put(month, new Reward((t.getCustId() + ":" + month), t.getCustId(), t.getName(), month, computeRewardPoint(t.getAmount()), t.getAmount()));
                rewardMap.put(t.getCustId(), monthlyMap);
            } else {
                monthlyMap = rewardMap.get(t.getCustId());
                if (monthlyMap.containsKey(month)) {
                    r = monthlyMap.get(month);
                    r.setPoints(r.getPoints() + computeRewardPoint(t.getAmount()));
                    r.setAmountSpent(r.getAmountSpent()+t.getAmount());
                } else {
                    r = new Reward((t.getCustId() + ":" + month), t.getCustId(), t.getName(), month, computeRewardPoint(t.getAmount()),t.getAmount());
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
    }

    @Override
    public ResponseEntity<List<RewardResponse>> createRewardResponse() {
        RewardResponse rewardResponse = new RewardResponse();
        List<RewardResponse> rewardResponseList = new ArrayList<>();
        List<Reward> rewardList = new ArrayList<>();
        List<String> custIdList = new ArrayList<>();
        custIdList = rewardsRepository.findCustId();
        int totalRewards = 0;
        int amountSpent = 0;
        for (String id : custIdList) {
            rewardList = rewardsRepository.findByCustId(id);
            rewardResponse.setName(rewardList.get(0).getName());
            rewardResponse.setCustId(id);
            List<RewardDetail> rewardDetails = new ArrayList<>();
            for (Reward reward : rewardList) {
                rewardDetails.add(new RewardDetail(reward.getMonth(), reward.getPoints(), reward.getAmountSpent()));
                totalRewards += reward.getPoints();
                amountSpent += reward.getAmountSpent();
            }
            Collections.sort(rewardDetails,(i,j)-> monthSort(i.getMonth())>monthSort(j.getMonth())?1:-1);
            rewardResponse.setTotal_points(totalRewards);
            rewardResponse.setRewardDetails(rewardDetails);
            rewardResponse.setTotalAmountSpent(amountSpent);
            rewardResponseList.add(rewardResponse);
            totalRewards = 0;
            amountSpent = 0;
            rewardResponse = new RewardResponse();
        }
        return new ResponseEntity<>(rewardResponseList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RewardResponse> createRewardResponse(String custId) {
        List<Reward> rewards = rewardsRepository.findByCustId(custId);
        if (rewards.size() == 0)
            throw new CustomerIdNotFoundException("Customer Id " + custId + " not present in DB", "Id does not exist");
        RewardResponse rewardResponse = new RewardResponse();
        rewardResponse.setCustId(custId);
        rewardResponse.setName(rewards.get(0).getName());
        List<RewardDetail> rewardDetails = new ArrayList<>();
        int totalPoints = 0;
        int totalAmountSpent = 0;
        for (Reward r : rewards) {
            rewardDetails.add(new RewardDetail(r.getMonth(), r.getPoints(), r.getAmountSpent()));
            totalPoints += r.getPoints();
            totalAmountSpent += r.getAmountSpent();
        }
        Collections.sort(rewardDetails,(i,j)-> monthSort(i.getMonth())>monthSort(j.getMonth())?1:-1);
        rewardResponse.setRewardDetails(rewardDetails);
        rewardResponse.setTotal_points(totalPoints);
        rewardResponse.setTotalAmountSpent(totalAmountSpent);
        return new ResponseEntity<>(rewardResponse, HttpStatus.OK);
    }

    private int monthSort(String month){
        HashMap<String,Integer> map= new HashMap<>();
        map.put("January",1);
        map.put("February",2);
        map.put("March",3);
        map.put("April",4);
        map.put("May",5);
        map.put("June",6);
        map.put("July",7);
        map.put("August",8);
        map.put("September",9);
        map.put("October",10);
        map.put("November",11);
        map.put("December",12);

        return map.get(month);
    }

    public int computeRewardPoint(int amount) {
        if (amount <= 50) return 0;
        else if (amount <= 100) return (amount - 50);
        else return 2 * (amount - 100) + 50;
    }
}
