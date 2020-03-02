package id.halalvestor.model;

import java.io.Serializable;
import java.util.Map;

public class InsuranceProduct implements Serializable {

    private int id;
    private Long portfolio;
    private String name;
    private String type;
    private String provider;
    private Double premium;
    private Double adminFee;
    private String frequency;
    private boolean recommended;
    private String link;
    private Map<String,String> description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Long portfolio) {
        this.portfolio = portfolio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Double getPremium() {
        return premium;
    }

    public void setPremium(Double premium) {
        this.premium = premium;
    }

    public Double getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(Double adminFee) {
        this.adminFee = adminFee;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "InsuranceProduct{" +
                "id=" + id +
                ", portfolio=" + portfolio +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", provider='" + provider + '\'' +
                ", premium=" + premium +
                ", adminFee=" + adminFee +
                ", frequency='" + frequency + '\'' +
                ", recommended=" + recommended +
                ", link='" + link + '\'' +
                ", description=" + description +
                '}';
    }
}
