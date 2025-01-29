package CustomerRewardsApp.customerRewardsService;

import CustomerRewardsApp.models.Reward;
import CustomerRewardsApp.models.RewardDTO;
import CustomerRewardsApp.models.Transaction;

import java.util.List;

public interface RewardsService {
    List<RewardDTO> calculateRewardPoints();
    RewardDTO calculateRewardPoints(String custId);
}
