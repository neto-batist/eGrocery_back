package com.egrocery_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqsFilterDTO {

    @Getter
    private String question;
    private Boolean isFrequentlyAsked;  // Novo campo para filtrar por perguntas frequentes

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getIsFrequentlyAsked() {
        return isFrequentlyAsked;
    }

    public void setFrequentlyAsked(Boolean frequentlyAsked) {
        isFrequentlyAsked = frequentlyAsked;
    }
}
