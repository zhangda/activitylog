package cn.rfidcn.activitylog.model;

public abstract class ActivityUserTagEvent extends ActivityUserEvent {

    private String honestId;
    private String sequenceNum;


    public String getHonestId() {
        return this.honestId;
    }

    public void setHonestId(String honestId) {
        this.honestId = honestId;
    }

    public String getSequenceNumber() {
        return this.sequenceNum;
    }

    public void setSequenceNumber(String sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    
}
