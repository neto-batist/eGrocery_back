package com.egrocery_back.controllers;

import com.egrocery_back.dto.OfferDTO;
import com.egrocery_back.dto.OfferFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OfferService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offers")
@Validated
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<Page<OfferDTO>> getAllOffers(
            @Valid OfferFilterDTO filter,
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "startDate") // ordenação padrão por data de início
            }) Pageable pageable
    ) {
        Page<OfferDTO> offers = offerService.getAllOffers(filter,pageable);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable @Min(1) Integer id) throws NotFound {
        OfferDTO offer = offerService.getOfferById(id);
        return ResponseEntity.ok(offer);
    }

    @PostMapping("/create")
    public ResponseEntity<OfferDTO> createOffer(@RequestBody @Valid OfferDTO offerDTO) throws NotFound {
        OfferDTO createdOffer = offerService.saveOffer(offerDTO);
        return ResponseEntity.ok(createdOffer);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OfferDTO> updateOffer(
            @PathVariable @Min(1) Integer id,
            @RequestBody @Valid OfferDTO offerDTO) throws NotFound {
        OfferDTO updatedOffer = offerService.updateOffer(id, offerDTO);
        return ResponseEntity.ok(updatedOffer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable @Min(1) Integer id) throws NotFound {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}
