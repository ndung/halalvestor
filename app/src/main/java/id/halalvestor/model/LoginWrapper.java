package id.halalvestor.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LoginWrapper implements Serializable {

    private AppUser appUser;
    private boolean newUser;
    private Map<Integer,List<QuestionParameter>> surveyAnswers;

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public Map<Integer, List<QuestionParameter>> getSurveyAnswers() {
        return surveyAnswers;
    }

    public void setSurveyAnswers(Map<Integer, List<QuestionParameter>> surveyAnswers) {
        this.surveyAnswers = surveyAnswers;
    }
}
