package cn.rfidcn.activitylog.model;


public class ActivityUserScan extends ActivityUserTagEvent {

    private int batchId;
    private int companyId;
    private int productId;
    private int specId;

    public int getBatchId() {
        return this.batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSpecId() {
        return this.specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

    
  
}
