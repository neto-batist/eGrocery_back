package com.egrocery_back.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egrocery_back.dto.OfferDTO;
import com.egrocery_back.dto.OfferFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.OfferService;

public class OfferControllerTest {

	@InjectMocks
	private OfferController offerController;

	@Mock
	private OfferService offerService;

	private OfferDTO offerDTO;
	private OfferFilterDTO offerFilterDTO;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		offerDTO = Mockito.mock(OfferDTO.class);
		offerFilterDTO = new OfferFilterDTO();
		pageable = PageRequest.of(0, 10);
	}

	@Test
	void getAllOffers_NoFilter_ReturnsOkWithOfferPage() {
		List<OfferDTO> offersList = Collections.singletonList(offerDTO);
		Page<OfferDTO> offerPage = new PageImpl<>(offersList, pageable, offersList.size());
		when(offerService.getAllOffers(any(OfferFilterDTO.class), any(Pageable.class))).thenReturn(offerPage);

		ResponseEntity<Page<OfferDTO>> response = offerController.getAllOffers(offerFilterDTO, pageable);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(offerPage, response.getBody());
		verify(offerService, times(1)).getAllOffers(offerFilterDTO, pageable);
	}

	@Test
	void getOfferById_ValidId_ReturnsOkWithOfferDTO() throws NotFound {
		int offerId = 1;
		when(offerService.getOfferById(offerId)).thenReturn(offerDTO);

		ResponseEntity<OfferDTO> response = offerController.getOfferById(offerId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(offerDTO, response.getBody());
		verify(offerService, times(1)).getOfferById(offerId);
	}

	@Test
	void getOfferById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int offerId = 1;
		when(offerService.getOfferById(offerId)).thenThrow(new NotFound("Oferta não encontrada", 1));

		assertThrows(NotFound.class, () -> offerController.getOfferById(offerId));
		verify(offerService, times(1)).getOfferById(offerId);
	}

	@Test
    void createOffer_ValidInput_ReturnsOkWithCreatedOfferDTO() throws NotFound {
        when(offerService.saveOffer(offerDTO)).thenReturn(offerDTO);

        ResponseEntity<OfferDTO> response = offerController.createOffer(offerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(offerDTO, response.getBody());
        verify(offerService, times(1)).saveOffer(offerDTO);
    }

	@Test
    void createOffer_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
        when(offerService.saveOffer(offerDTO)).thenThrow(new NotFound("Erro ao salvar a oferta",1));

        assertThrows(NotFound.class, () -> offerController.createOffer(offerDTO));
        verify(offerService, times(1)).saveOffer(offerDTO);
    }

	@Test
	void updateOffer_ValidInput_ReturnsOkWithUpdatedOfferDTO() throws NotFound {
		int offerId = 1;
		OfferDTO updatedOfferDTO = new OfferDTO();
		updatedOfferDTO.setId(offerId);
		updatedOfferDTO.setDiscountPercentage(15.0);
		when(offerService.updateOffer(offerId, offerDTO)).thenReturn(updatedOfferDTO);

		ResponseEntity<OfferDTO> response = offerController.updateOffer(offerId, offerDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedOfferDTO, response.getBody());
		verify(offerService, times(1)).updateOffer(offerId, offerDTO);
	}

	@Test
	void updateOffer_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int offerId = 1;
		when(offerService.updateOffer(offerId, offerDTO)).thenThrow(new NotFound("Oferta não encontrada", 1));

		assertThrows(NotFound.class, () -> offerController.updateOffer(offerId, offerDTO));
		verify(offerService, times(1)).updateOffer(offerId, offerDTO);
	}

	@Test
	void deleteOffer_ValidId_ReturnsNoContent() throws NotFound {
		int offerId = 1;
		doNothing().when(offerService).deleteOffer(offerId);

		ResponseEntity<Void> response = offerController.deleteOffer(offerId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(offerService, times(1)).deleteOffer(offerId);
	}

	@Test
	void deleteOffer_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int offerId = 1;
		doThrow(new NotFound("Oferta não encontrada", 1)).when(offerService).deleteOffer(offerId);

		assertThrows(NotFound.class, () -> offerController.deleteOffer(offerId));
		verify(offerService, times(1)).deleteOffer(offerId);
	}
}