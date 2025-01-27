package CustomerRewardsApp.models;

import java.util.HashMap;

public class Reward {
    private String custId;
    private HashMap<String, Integer> monthlyRewards;
    private int totalRewards;

    public HashMap<String, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(HashMap<String, Integer> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    public int getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(int totalRewards) {
        this.totalRewards = totalRewards;
    }

    public Reward(String custId) {
        this.custId = custId;
        this.monthlyRewards = new HashMap<>();
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Reward() {
        this.setMonthlyRewards(new HashMap<>());
    }
}
