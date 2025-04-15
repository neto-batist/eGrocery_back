package com.egrocery_back.services;

import com.egrocery_back.dto.FaqsDTO;
import com.egrocery_back.dto.FaqsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.FaqsEntity;
import com.egrocery_back.repositories.FaqsRepository;
import com.egrocery_back.specifications.FaqsSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class FaqsServiceImpl implements FaqsService {

    private final FaqsRepository faqsRepository;

    public FaqsServiceImpl(FaqsRepository faqsRepository) {
        this.faqsRepository = faqsRepository;
    }

    // Mapeia a entidade para DTO
    private FaqsDTO mapToDTO(FaqsEntity entity) {
        FaqsDTO dto = new FaqsDTO();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());

        if (entity.getCreatedAt() != null) {
            dto.setCreatedAt(entity.getCreatedAt().toLocalDateTime());
        }

        if (entity.getUpdatedAt() != null) {
            dto.setUpdatedAt(entity.getUpdatedAt().toLocalDateTime());
        }

        return dto;
    }

    // Mapeia o DTO para a entidade
    private FaqsEntity mapToEntity(FaqsDTO dto) {
        FaqsEntity entity = new FaqsEntity();
        entity.setId(dto.getId());
        entity.setQuestion(dto.getQuestion());
        entity.setAnswer(dto.getAnswer());

        if (dto.getCreatedAt() != null) {
            entity.setCreatedAt(Timestamp.valueOf(dto.getCreatedAt()));
        }

        if (dto.getUpdatedAt() != null) {
            entity.setUpdatedAt(Timestamp.valueOf(dto.getUpdatedAt()));
        }

        return entity;
    }

    @Override
    @Transactional
    public FaqsDTO createFaq(FaqsDTO faqsDTO) {
        FaqsEntity entity = mapToEntity(faqsDTO);
        entity.setCreatedAt(Timestamp.from(Instant.now()));
        FaqsEntity savedEntity = faqsRepository.save(entity);
        return mapToDTO(savedEntity);
    }

    @Override
    @Transactional
    public FaqsDTO updateFaq(Integer faqId, FaqsDTO faqsDTO) throws NotFound {
        FaqsEntity existingFaq = faqsRepository.findById(faqId)
                .orElseThrow(() -> new NotFound("FAQ", faqId));

        existingFaq.setQuestion(faqsDTO.getQuestion());
        existingFaq.setAnswer(faqsDTO.getAnswer());
        existingFaq.setUpdatedAt(Timestamp.from(Instant.now()));

        FaqsEntity updatedEntity = faqsRepository.save(existingFaq);
        return mapToDTO(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteFaq(Integer faqId) throws NotFound {
        FaqsEntity faq = faqsRepository.findById(faqId)
                .orElseThrow(() -> new NotFound("FAQ", faqId));
        faqsRepository.delete(faq);
    }

    @Override
    public Page<FaqsDTO> getAllFaqs(FaqsFilterDTO filter, Pageable pageable) {
        return faqsRepository.findAll(FaqsSpecification.filter(filter), pageable)
                .map(this::mapToDTO);
    }

    @Override
    public FaqsDTO getFaqById(Integer faqId) throws NotFound {
        FaqsEntity faq = faqsRepository.findById(faqId)
                .orElseThrow(() -> new NotFound("FAQ", faqId));
        return mapToDTO(faq);
    }
}
