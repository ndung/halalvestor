/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.halalvestor.model;

import java.util.Map;

public class RiskProfile {

    private int id;
    private String name;
    private String description;
    private Double investmentRate;
    private Map<ProductType,Double> productRecommendations;
    private Double monthlyInstallment;
    private boolean recommended;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getInvestmentRate() {
        return investmentRate;
    }

    public void setInvestmentRate(Double investmentRate) {
        this.investmentRate = investmentRate;
    }

    public Map<ProductType, Double> getProductRecommendations() {
        return productRecommendations;
    }

    public void setProductRecommendations(Map<ProductType, Double> productRecommendations) {
        this.productRecommendations = productRecommendations;
    }

    public Double getMonthlyInstallment() {
        return monthlyInstallment;
    }

    public void setMonthlyInstallment(Double monthlyInstallment) {
        this.monthlyInstallment = monthlyInstallment;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RiskProfile other = (RiskProfile) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RiskProfile{" + "id=" + id + ", name=" + name + ", description=" + description + ", investmentRate=" + investmentRate + '}';
    }

}
