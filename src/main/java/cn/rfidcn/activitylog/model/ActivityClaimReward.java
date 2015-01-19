package cn.rfidcn.activitylog.model;

public class ActivityClaimReward extends ActivityUserScan {
    private String rewardAmount;

    public String getRewardAmount() {
        return this.rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

}
