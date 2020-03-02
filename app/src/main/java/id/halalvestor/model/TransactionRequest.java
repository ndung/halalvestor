package id.halalvestor.model;


public class TransactionRequest {

    private int type;
    private InvestmentProduct investmentProduct;
    private InsuranceProduct insuranceProduct;
    private double amount;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public InvestmentProduct getInvestmentProduct() {
        return investmentProduct;
    }

    public void setInvestmentProduct(InvestmentProduct investmentProduct) {
        this.investmentProduct = investmentProduct;
    }

    public InsuranceProduct getInsuranceProduct() {
        return insuranceProduct;
    }

    public void setInsuranceProduct(InsuranceProduct insuranceProduct) {
        this.insuranceProduct = insuranceProduct;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
