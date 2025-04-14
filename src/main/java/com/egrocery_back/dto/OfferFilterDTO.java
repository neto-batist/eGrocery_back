package com.egrocery_back.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class OfferFilterDTO {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @AssertTrue(message = "A data de início deve ser anterior ou igual à data de término")
    public boolean isValidDateRange() {
        if (startDate != null && endDate != null) {
            return !startDate.isAfter(endDate);
        }
        return true;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
