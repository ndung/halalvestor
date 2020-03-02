package id.halalvestor.model;

public class BuySell {

    private String productName;
    private String type;
    private Double nab;
    private Integer growth;
    private Double own;

    public BuySell(String productName, String type, Double nab, Integer growth, Double own) {
        this.productName = productName;
        this.type = type;
        this.nab = nab;
        this.growth = growth;
        this.own = own;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getNab() {
        return nab;
    }

    public void setNab(Double nab) {
        this.nab = nab;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Double getOwn() {
        return own;
    }

    public void setOwn(Double own) {
        this.own = own;
    }
}
