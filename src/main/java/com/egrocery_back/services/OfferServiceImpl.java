package com.egrocery_back.services;

import com.egrocery_back.dto.OfferDTO;
import com.egrocery_back.dto.OfferFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.models.OffersEntity;
import com.egrocery_back.models.ProductsEntity;
import com.egrocery_back.repositories.OfferRepository;
import com.egrocery_back.repositories.ProductRepository;
import com.egrocery_back.specifications.OfferSpecification;
import com.egrocery_back.specifications.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            ProductRepository productRepository, ProductServiceImpl productService) {
        this.offerRepository = offerRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    public OfferDTO saveOffer(OfferDTO dto) throws NotFound {
        OffersEntity entity = mapToEntity(dto);
        OffersEntity saved = offerRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public Page<OfferDTO> getAllOffers(OfferFilterDTO filterDTO, Pageable pageable) {

        return offerRepository.findAll(OfferSpecification.filter(filterDTO), pageable)
                .map(this::mapToDTO);
    }

    @Override
    public OfferDTO getOfferById(Integer id) throws NotFound {
        OffersEntity entity = findOfferById(id);
        return mapToDTO(entity);
    }

    @Override
    public OfferDTO updateOffer(Integer id, OfferDTO dto) throws NotFound {
        OffersEntity existing = findOfferById(id);

        OffersEntity updatedEntity = mapToEntity(dto);
        updatedEntity.setId(id);
        OffersEntity updated = offerRepository.save(updatedEntity);

        return mapToDTO(updated);
    }

    @Override
    public void deleteOffer(Integer id) throws NotFound {
        OffersEntity offer = findOfferById(id);
        offerRepository.delete(offer);
    }


    public OffersEntity mapToEntity(OfferDTO dto) throws NotFound {
        OffersEntity entity = new OffersEntity();
        entity.setId(dto.getId());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setProductsByProductId(getProductEntity(dto.getProduct().getId()));
        return entity;
    }

    public OfferDTO mapToDTO(OffersEntity entity) {
        OfferDTO dto = new OfferDTO();
        dto.setId(entity.getId());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setProduct(productService.mapToDTO(entity.getProductsByProductId()));
        return dto;
    }

    private OffersEntity findOfferById(Integer id) throws NotFound {
        return offerRepository.findById(id)
                .orElseThrow(() -> new NotFound("Oferta", id));
    }

    private ProductsEntity getProductEntity(Integer productId) throws NotFound {
        if (productId == null) return null;
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFound("Produto", productId));
    }
}
