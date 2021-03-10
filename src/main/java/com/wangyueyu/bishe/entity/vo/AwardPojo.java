package com.wangyueyu.bishe.entity.vo;

public class AwardPojo implements java.io.Serializable, Cloneable {
    /**
     * 淘宝ID 手机号
     */
    private String buyerName;

    /**
     * 活动名称 官网订单号
     */
    private String ecsOrderId;

    public AwardPojo(String buyerName,String ecsOrderId){
        this.buyerName = buyerName;
        this.ecsOrderId = ecsOrderId;
    }

    public AwardPojo(){

    }
    public String getEcsOrderId() {
        return ecsOrderId;
    }

    public void setEcsOrderId(String ecsOrderId) {
        this.ecsOrderId = ecsOrderId;
    }

    public String getBuyerName() {

        return buyerName;
    }

    @Override
    public String toString() {
        return "AwardPojo{" +
                "buyerName='" + buyerName + '\'' +
                ", ecsOrderId='" + ecsOrderId + '\'' +
                '}';
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}