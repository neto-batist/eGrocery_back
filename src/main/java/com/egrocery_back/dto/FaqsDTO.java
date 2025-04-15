package com.egrocery_back.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class FaqsDTO {

    private Integer id;

    @NotBlank(message = "A pergunta não pode estar em branco")  // Validação para garantir que a pergunta não seja em branco
    private String question;
    
    @NotBlank(message = "A resposta não pode estar em branco")
    private String answer;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
