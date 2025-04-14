package com.egrocery_back.services;

import com.egrocery_back.dto.OfferDTO;
import com.egrocery_back.dto.OfferFilterDTO;
import com.egrocery_back.errors.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferService {

    OfferDTO saveOffer(OfferDTO dto) throws NotFound;

    Page<OfferDTO> getAllOffers(OfferFilterDTO filterDTO, Pageable pageable);

    OfferDTO getOfferById(Integer id) throws NotFound;

    OfferDTO updateOffer(Integer id, OfferDTO dto) throws NotFound;

    void deleteOffer(Integer id) throws NotFound;
}
