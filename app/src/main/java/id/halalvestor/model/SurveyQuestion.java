package id.halalvestor.model;

import java.io.Serializable;
import java.util.List;

public class SurveyQuestion implements Serializable {

    Integer id;
    Integer section;
    String question;
    Integer type;
    boolean required;
    List<QuestionParameter> parameters;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<QuestionParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<QuestionParameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "SurveyQuestion{" +
                "id=" + id +
                ", section=" + section +
                ", question='" + question + '\'' +
                ", type=" + type +
                ", required=" + required +
                ", parameters=" + parameters +
                '}';
    }
}
