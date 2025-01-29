package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardResponse;
import CustomerRewardsApp.models.Transaction;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RewardsService {
    ResponseEntity<List<RewardResponse>> createRewardResponse();
    ResponseEntity<RewardResponse> createRewardResponse(String custId);
}
