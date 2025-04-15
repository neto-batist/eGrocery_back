package com.egrocery_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqsFilterDTO {

    private String question;
    private Boolean isFrequentlyAsked;  // Novo campo para filtrar por perguntas frequentes

}
