package id.halalvestor.model;

import java.util.Date;

public class InvestmentGoal {

    private Long id;
    private String goal;
    private Integer time;
    private Date dateTime;
    private Double fund;
    private Double fv;
    private Double installment;
    private String pic;
    private boolean achieved;
    private int progress;
    private boolean previousGoalAchieved;
    private double investment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Double getFund() {
        return fund;
    }

    public void setFund(Double fund) {
        this.fund = fund;
    }

    public Double getFv() {
        return fv;
    }

    public void setFv(Double fv) {
        this.fv = fv;
    }

    public Double getInstallment() {
        return installment;
    }

    public void setInstallment(Double installment) {
        this.installment = installment;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public boolean isPreviousGoalAchieved() {
        return previousGoalAchieved;
    }

    public void setPreviousGoalAchieved(boolean previousGoalAchieved) {
        this.previousGoalAchieved = previousGoalAchieved;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getInvestment() {
        return investment;
    }

    public void setInvestment(double investment) {
        this.investment = investment;
    }

    @Override
    public String toString() {
        return "InvestmentGoal{" +
                "id=" + id +
                ", goal='" + goal + '\'' +
                ", time=" + time +
                ", dateTime=" + dateTime +
                ", fund=" + fund +
                ", fv=" + fv +
                ", installment=" + installment +
                '}';
    }
}
