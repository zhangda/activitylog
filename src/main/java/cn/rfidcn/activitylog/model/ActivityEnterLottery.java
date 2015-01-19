package cn.rfidcn.activitylog.model;

public class ActivityEnterLottery extends ActivityUserScan {

    private int promoRuleId;
    private String rewardType;
    private String rewardAmount;



    public int getPromoRuleId() {
        return this.promoRuleId;
    }

    public void setPromoRuleId(int promoRuleId) {
        this.promoRuleId = promoRuleId;
    }

    public String getRewardType() {
        return this.rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardAmount() {
        return this.rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }

}
