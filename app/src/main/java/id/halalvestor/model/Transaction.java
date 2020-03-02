package id.halalvestor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Transaction implements Serializable {

    private Long id;
    private String orderNo;
    private Date trxDate;
    private int trxType;
    private Long productId;
    //private InvestmentProduct product;
    private int status;
    private double netAmount;
    /**private double nab;
     private String instruction;
     private double unit;*/
    private double fee;
    private double totalAmount;
    private int productCategory;
    private String productCategoryName;
    private Map<String,String> detail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(Date trxDate) {
        this.trxDate = trxDate;
    }

    public int getTrxType() {
        return trxType;
    }

    public void setTrxType(int trxType) {
        this.trxType = trxType;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**public InvestmentProduct getInvestmentProduct() {
     return product;
     }

     public void setInvestmentProduct(InvestmentProduct product) {
     this.product = product;
     }*/



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    /**public double getNab() {
     return nab;
     }

     public void setNab(double nab) {
     this.nab = nab;
     }

     public String getInstruction() {
     return instruction;
     }

     public void setInstruction(String instruction) {
     this.instruction = instruction;
     }

     public double getUnit() {
     return unit;
     }

     public void setUnit(double unit) {
     this.unit = unit;
     }*/

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public Map<String, String> getDetail() {
        return detail;
    }

    public void setDetail(Map<String, String> detail) {
        this.detail = detail;
    }
}
