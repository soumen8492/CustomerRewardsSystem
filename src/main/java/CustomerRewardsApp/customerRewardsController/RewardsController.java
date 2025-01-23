package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardsController {
    @Autowired
    RewardsServiceImpl rewardsService;
    @GetMapping("/allTransaction")
    public List<Transaction> getAllTransactions()
    {
        return rewardsService.getAllTransaction();
    }
    @PatchMapping("/calcRewards")
    public List<Transaction> calculateRewardPoint()
    {
        return rewardsService.updateRewardPoint();
    }
    @GetMapping("/transactions/{customerId}/{month}")
    public List<Transaction> getByCustIdAndMonth(@PathVariable("customerId") String custId, @PathVariable("month") String month)
    {
        return rewardsService.getTransactionByCustIdAndMonth(custId, month);
    }
    @GetMapping("/transactions/{customerId}")
    public List<Transaction> getByCustId(@PathVariable("customerId") String custId)
    {
        return rewardsService.getTransactionByCustId(custId);
    }
    @GetMapping("/total")
    public List<Reward> getTotal()
    {
        return rewardsService.getTotalReward();
    }
    @GetMapping("/total/{customerId}")
    public Reward getTotalByCustId(@PathVariable("customerId") String custId)
    {
        return rewardsService.getTotalReward(custId);
    }
    @GetMapping("/total/{customerId}/{month}")
    public Reward getTotalByCustIdAndMonth(@PathVariable("customerId") String custId, @PathVariable("month") String month)
    {
        return rewardsService.getTotalReward(custId, month);
    }
}
