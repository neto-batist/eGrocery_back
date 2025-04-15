package com.egrocery_back.services;

import com.egrocery_back.dto.FaqsDTO;
import com.egrocery_back.dto.FaqsFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqsService {

    // Criar uma nova FAQ
    FaqsDTO createFaq(FaqsDTO faqsDTO);

    // Atualizar uma FAQ existente
    FaqsDTO updateFaq(Integer faqId, FaqsDTO faqsDTO) throws NotFound;

    // Deletar uma FAQ
    void deleteFaq(Integer faqId) throws NotFound;

    // Buscar todas as FAQs com filtros e paginação
    Page<FaqsDTO> getAllFaqs(FaqsFilterDTO filter, Pageable pageable);

    // Buscar uma FAQ pelo ID
    FaqsDTO getFaqById(Integer faqId) throws NotFound;
}
