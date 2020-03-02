package id.halalvestor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AppUser implements Serializable{

    private Date regDate;
    private Double highRisk;
    private Double mediumRisk;
    private Double lowRisk;
    private Double totalInvestment;
    private List<InvestmentGoal> investmentGoals;
    private Date birthDate;
    private Double monthlyIncome;
    private Double monthlyInstallment;
    private String maritalStatus;
    private String gender;
    private String education;
    private String occupation;
    private String phoneNumber;
    private Map<ProductType,Double> productRecommendations;

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Double getHighRisk() {
        return highRisk;
    }

    public void setHighRisk(Double highRisk) {
        this.highRisk = highRisk;
    }

    public Double getMediumRisk() {
        return mediumRisk;
    }

    public void setMediumRisk(Double mediumRisk) {
        this.mediumRisk = mediumRisk;
    }

    public Double getLowRisk() {
        return lowRisk;
    }

    public void setLowRisk(Double lowRisk) {
        this.lowRisk = lowRisk;
    }

    public Double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(Double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public List<InvestmentGoal> getInvestmentGoals() {
        return investmentGoals;
    }

    public void setInvestmentGoals(List<InvestmentGoal> investmentGoals) {
        this.investmentGoals = investmentGoals;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(Double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(Double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private Integer riskProfile;
    private Integer investmentOption;
    private List<RiskProfile> profileOptions;

    public Integer getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(Integer riskProfile) {
        this.riskProfile = riskProfile;
    }

    public Integer getInvestmentOption() {
        return investmentOption;
    }

    public void setInvestmentOption(Integer investmentOption) {
        this.investmentOption = investmentOption;
    }

    public List<RiskProfile> getProfileOptions() {
        return profileOptions;
    }

    public void setProfileOptions(List<RiskProfile> profileOptions) {
        this.profileOptions = profileOptions;
    }

    public Map<ProductType, Double> getProductRecommendations() {
        return productRecommendations;
    }

    public void setProductRecommendations(Map<ProductType, Double> productRecommendations) {
        this.productRecommendations = productRecommendations;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "regDate=" + regDate +
                ", highRisk=" + highRisk +
                ", mediumRisk=" + mediumRisk +
                ", lowRisk=" + lowRisk +
                ", totalInvestment=" + totalInvestment +
                ", investmentGoals=" + investmentGoals +
                ", birthDate=" + birthDate +
                ", monthlyIncome=" + monthlyIncome +
                ", monthlyInstallment=" + monthlyInstallment +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", education='" + education + '\'' +
                ", occupation='" + occupation + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", riskProfile=" + riskProfile +
                ", investmentOption=" + investmentOption +
                ", profileOptions=" + profileOptions +
                '}';
    }
}
