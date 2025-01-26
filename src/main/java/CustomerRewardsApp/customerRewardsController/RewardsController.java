package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rewards")
public class RewardsController {
    @Autowired
    RewardsServiceImpl rewardsService;
    @GetMapping("/allTransaction")
    public ResponseEntity<?> getAllTransactions()
    {
        return ResponseEntity.ok(rewardsService.getAllTransaction());
    }
    @PatchMapping("/calcRewards")
    public ResponseEntity<?> calculateRewardPoint()
    {
        return ResponseEntity.ok(rewardsService.updateRewardPoint());
    }
    @GetMapping("/transactions/{customerId}/{month}")
    public ResponseEntity<?> getByCustIdAndMonth(@PathVariable("customerId") String custId, @PathVariable("month") String month)
    {
        return ResponseEntity.ok(rewardsService.getTransactionByCustIdAndMonth(custId, month));
    }
    @GetMapping("/transactions/{customerId}")
    public ResponseEntity<?> getByCustId(@PathVariable("customerId") String custId)
    {
        return ResponseEntity.ok(rewardsService.getTransactionByCustId(custId));
    }
    @GetMapping("/total")
    public List<Reward> getTotal()
    {
        return rewardsService.getTotalReward();
    }
    @GetMapping("/total/{customerId}")
    public ResponseEntity<?> getTotalByCustId(@PathVariable("customerId") String custId)
    {
        return ResponseEntity.ok(rewardsService.getTotalReward(custId));
    }
    @GetMapping("/total/{customerId}/{month}")
    public ResponseEntity<?> getTotalByCustIdAndMonth(@PathVariable("customerId") String custId, @PathVariable("month") String month)
    {
        return ResponseEntity.ok(rewardsService.getTotalReward(custId, month));
    }
}
