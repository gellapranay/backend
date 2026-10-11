package com.antarmarg.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FeedbackRequest {

    @NotNull(message = "Rating is required")
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;
    private Integer difficultyScore;
    private Boolean wasAccurate = true;

    public FeedbackRequest() {
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDifficultyScore() {
        return difficultyScore;
    }

    public void setDifficultyScore(Integer difficultyScore) {
        this.difficultyScore = difficultyScore;
    }

    public Boolean getWasAccurate() {
        return wasAccurate;
    }

    public void setWasAccurate(Boolean wasAccurate) {
        this.wasAccurate = wasAccurate;
    }
}
