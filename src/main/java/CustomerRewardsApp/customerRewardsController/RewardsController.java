package CustomerRewardsApp.customerRewardsController;

import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardResponse;
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
    public ResponseEntity<List<RewardResponse>> getTotal()
    {
        return rewardsService.getRewardResponse();
    }
    @GetMapping("/total/{customerId}")
    public ResponseEntity<RewardResponse> getTotalByCustId(@PathVariable("customerId") String custId)
    {
        return rewardsService.getRewardResponse(custId);
    }
}
