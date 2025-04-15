package com.egrocery_back.controllers;

import com.egrocery_back.dto.FaqsDTO;
import com.egrocery_back.dto.FaqsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.FaqsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faqs")
public class FaqsController {

    private final FaqsService faqsService;

    public FaqsController(FaqsService faqsService) {
        this.faqsService = faqsService;
    }

    @PostMapping("/create")
    public FaqsDTO createFaq(@Valid @RequestBody FaqsDTO faqsDTO) {
        return faqsService.createFaq(faqsDTO);
    }

    @PutMapping("/update/{id}")
    public FaqsDTO updateFaq(@PathVariable("id") Integer id, @Valid @RequestBody FaqsDTO faqsDTO) throws NotFound {
        return faqsService.updateFaq(id, faqsDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFaq(@PathVariable("id") Integer id) throws NotFound {
        faqsService.deleteFaq(id);
    }

    @GetMapping("/all")
    public Page<FaqsDTO> getAllFaqs(FaqsFilterDTO filter, Pageable pageable) {
        return faqsService.getAllFaqs(filter, pageable);
    }

    @GetMapping("/{id}")
    public FaqsDTO getFaqById(@PathVariable("id") Integer id) throws NotFound {
        return faqsService.getFaqById(id);
    }
}
