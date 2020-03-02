package id.halalvestor.model;

import com.google.common.base.Objects;

import java.io.Serializable;

public class InvestmentProduct implements Serializable {

    private int id;
    private String productName;
    private Double nab;
    private Double boughtNab;
    private String productType;
    private Double growth;
    private Double unit;
    private Double amount;
    private String investmentManager;
    private Double adminFee;
    private Double minPurchase;
    private boolean recommended;
    private String link;
    private int goodRecommendation;
    private int carefulRecommendation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getNab() {
        return nab;
    }

    public void setNab(Double nab) {
        this.nab = nab;
    }

    public Double getBoughtNab() {
        return boughtNab;
    }

    public void setBoughtNab(Double boughtNab) {
        this.boughtNab = boughtNab;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getGrowth() {
        return growth;
    }

    public void setGrowth(Double growth) {
        this.growth = growth;
    }

    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getInvestmentManager() {
        return investmentManager;
    }

    public void setInvestmentManager(String investmentManager) {
        this.investmentManager = investmentManager;
    }

    public Double getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(Double adminFee) {
        this.adminFee = adminFee;
    }

    public Double getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(Double minPurchase) {
        this.minPurchase = minPurchase;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getGoodRecommendation() {
        return goodRecommendation;
    }

    public void setGoodRecommendation(int goodRecommendation) {
        this.goodRecommendation = goodRecommendation;
    }

    public int getCarefulRecommendation() {
        return carefulRecommendation;
    }

    public void setCarefulRecommendation(int carefulRecommendation) {
        this.carefulRecommendation = carefulRecommendation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvestmentProduct)) return false;
        InvestmentProduct that = (InvestmentProduct) o;
        return id == that.id &&
                Objects.equal(productName, that.productName) &&
                Objects.equal(nab, that.nab) &&
                Objects.equal(productType, that.productType) &&
                Objects.equal(growth, that.growth) &&
                Objects.equal(unit, that.unit) &&
                Objects.equal(amount, that.amount) &&
                Objects.equal(investmentManager, that.investmentManager) &&
                Objects.equal(adminFee, that.adminFee) &&
                Objects.equal(minPurchase, that.minPurchase);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, productName, nab, productType, growth, unit, amount, investmentManager, adminFee, minPurchase);
    }

    @Override
    public String toString() {
        return "InvestmentProduct{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", nab=" + nab +
                ", boughtNab=" + boughtNab +
                ", productType='" + productType + '\'' +
                ", growth=" + growth +
                ", unit=" + unit +
                ", amount=" + amount +
                ", investmentManager='" + investmentManager + '\'' +
                ", adminFee=" + adminFee +
                ", minPurchase=" + minPurchase +
                ", recommended=" + recommended +
                ", link='" + link + '\'' +
                ", goodRecommendation=" + goodRecommendation +
                ", carefulRecommendation=" + carefulRecommendation +
                '}';
    }
}
