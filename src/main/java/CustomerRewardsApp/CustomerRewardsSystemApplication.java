package CustomerRewardsApp;

import CustomerRewardsApp.customerRewardsService.RewardsService;
import CustomerRewardsApp.customerRewardsService.RewardsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerRewardsSystemApplication implements CommandLineRunner {

    @Autowired
    RewardsServiceImpl rewardsService;
    public static void main(String[] args) {
        SpringApplication.run(CustomerRewardsSystemApplication.class);
    }

    /**This may not be Industry standards
     * but as the App only needs to fetch rewards data to display
     * created this code snippet to initialize the rewards table
      */
    @Override
    public void run(String... args) throws Exception {
        rewardsService.setRewardPoints();
    }
}
