package id.halalvestor.model;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Map;

public class QuestionParameter implements Serializable {

    Integer id;
    Integer sequence;
    String description;
    Integer score;
    Map<String,String> additionalResponses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Map<String, String> getAdditionalResponses() {
        return additionalResponses;
    }

    public void setAdditionalResponses(Map<String, String> additionalResponses) {
        this.additionalResponses = additionalResponses;
    }

    @Override
    public String toString() {
        return "QuestionParameter{" +
                "id=" + id +
                ", sequence=" + sequence +
                ", description='" + description + '\'' +
                ", score=" + score +
                ", additionalResponses=" + additionalResponses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionParameter parameter = (QuestionParameter) o;
        return Objects.equal(id, parameter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
