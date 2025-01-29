package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
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

    @GetMapping("/total")
    public List<RewardDTO> getTotal()
    {
        return rewardsService.calculateRewardPoints();
    }
    @GetMapping("/total/{customerId}")
    public ResponseEntity<RewardDTO> getTotalByCustId(@PathVariable("customerId") String custId)
    {
        return ResponseEntity.ok(rewardsService.calculateRewardPoints(custId));
    }
}
