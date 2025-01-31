package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.customerRewardsException.CustomerIdNotFoundException;
import CustomerRewardsApp.customerRewardsRepository.RewardsRepository;
import CustomerRewardsApp.customerRewardsRepository.TransactionRepository;
import CustomerRewardsApp.models.*;
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

    public void createRewardPoints()
    {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Reward> rewards = new ArrayList<>();
        for(Transaction transaction : transactions)
        {
            rewards.add(new Reward(transaction.getTransactionId(), transaction.getTransactionId(), computeRewardPoint(transaction.getAmount())));
        }
        rewardsRepository.saveAll(rewards);
    }
    @Override
    public ResponseEntity<List<RewardResponse>> getRewardResponse() {
        List<String> CustIdList = transactionRepository.findCustId();
        List<RewardResponse> rewardResponseList = new ArrayList<>();
        for(String custId : CustIdList)
        {
            List<Transaction> transactionsByCustId = transactionRepository.findTransactionsByCustId(custId);
            List<Reward> rewardsByCustId = rewardsRepository.findRewardsByCustId(custId);
            rewardResponseList.add(createRewardResponse(custId, transactionsByCustId, rewardsByCustId));
        }
        return new ResponseEntity<>(rewardResponseList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RewardResponse> getRewardResponse(String custId) {
        List<Transaction> transactionsByCustId = transactionRepository.findTransactionsByCustId(custId);
        List<Reward> rewardsByCustId = rewardsRepository.findRewardsByCustId(custId);
        if(transactionsByCustId.size()==0 || rewardsByCustId.size()==0) throw new CustomerIdNotFoundException("Customer Id "+ custId+" not in DB", "Customer ID does not exist");
        return new ResponseEntity<>(createRewardResponse(custId, transactionsByCustId,rewardsByCustId), HttpStatus.OK);
    }

    private RewardResponse createRewardResponse(String custId, List<Transaction> transactionsByCustId, List<Reward> rewardsByCustId) {
        RewardResponse rewardResponse = new RewardResponse();
        rewardResponse.setCustId(custId);
        rewardResponse.setName(transactionsByCustId.get(0).getName());
        List<RewardDetail> rewardDetails = new ArrayList<>();
        HashMap<String, RewardDetail> monthMap = new HashMap<>();
        for(int i=0; i<transactionsByCustId.size();i++)
        {
            Transaction tran = transactionsByCustId.get(i);
            String month = LocalDate.parse(tran.getTranDate(), DateTimeFormatter.
                            ofPattern("MM/dd/yyyy")).
                    format(DateTimeFormatter.
                            ofPattern("MMMM", Locale.ENGLISH));
            Reward reward = rewardsByCustId.get(i);
            if(monthMap.containsKey(month))
            {
                RewardDetail rewardDetail = monthMap.get(month);
                List<TransactionAndPoints> transactionAndPointsList = rewardDetail.getTransactionAndPoints();
                transactionAndPointsList.add(new TransactionAndPoints(tran.getTranDate(), tran.getAmount(), reward.getPoints()));
                rewardDetail.setTransactionAndPoints(transactionAndPointsList);
                rewardDetail.setAmountSpent(rewardDetail.getAmountSpent()+ tran.getAmount());
                rewardDetail.setPoints(rewardDetail.getPoints()+reward.getPoints());
                monthMap.put(month, rewardDetail);
            }
            else{
                RewardDetail rewardDetail = new RewardDetail(month, reward.getPoints(), tran.getAmount());
                List<TransactionAndPoints> transactionAndPointsList = new ArrayList<>();
                transactionAndPointsList.add(new TransactionAndPoints(tran.getTranDate(), tran.getAmount(), reward.getPoints()));
                rewardDetail.setTransactionAndPoints(transactionAndPointsList);
                monthMap.put(month, rewardDetail);
            }
            for(String m : monthMap.keySet()){
                rewardDetails.add(monthMap.get(m));
            }
            Collections.sort(rewardDetails,(j,k)-> monthSort(j.getMonth())>monthSort(k.getMonth())?1:-1);
            rewardResponse.setRewardDetails(rewardDetails);
            rewardResponse.setTotalAmountSpent(rewardDetails.stream().mapToInt(RewardDetail::getAmountSpent).sum());
            rewardResponse.setTotal_points(rewardDetails.stream().mapToInt(RewardDetail::getPoints).sum());
            rewardDetails = new ArrayList<>();
        }
        return rewardResponse;
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
