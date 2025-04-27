package com.egrocery_back.controllers;

import com.egrocery_back.dto.FaqsDTO;
import com.egrocery_back.dto.FaqsFilterDTO;
import com.egrocery_back.errors.NotFound;
import com.egrocery_back.services.FaqsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class FaqsControllerTest {

	@InjectMocks
	private FaqsController faqsController;

	@Mock
	private FaqsService faqsService;

	private FaqsDTO faqsDTO;
	private FaqsFilterDTO faqsFilterDTO;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		faqsDTO = new FaqsDTO();
		faqsDTO.setId(1);
		faqsDTO.setQuestion("Pergunta de teste");
		faqsDTO.setAnswer("Resposta de teste");
		faqsFilterDTO = new FaqsFilterDTO();
		pageable = PageRequest.of(0, 10);
	}

	@Test
    void createFaq_ValidInput_ReturnsCreatedFaqsDTO() {
        when(faqsService.createFaq(faqsDTO)).thenReturn(faqsDTO);

        FaqsDTO createdFaq = faqsController.createFaq(faqsDTO);

        assertEquals(faqsDTO, createdFaq);
        verify(faqsService, times(1)).createFaq(faqsDTO);
    }

	@Test
	void updateFaq_ValidInput_ReturnsUpdatedFaqsDTO() throws NotFound {
		int faqId = 1;
		FaqsDTO updatedFaqsDTO = new FaqsDTO();
		updatedFaqsDTO.setId(faqId);
		updatedFaqsDTO.setQuestion("Pergunta atualizada");
		updatedFaqsDTO.setAnswer("Resposta atualizada");
		when(faqsService.updateFaq(faqId, faqsDTO)).thenReturn(updatedFaqsDTO);

		FaqsDTO updatedFaq = faqsController.updateFaq(faqId, faqsDTO);

		assertEquals(updatedFaqsDTO, updatedFaq);
		verify(faqsService, times(1)).updateFaq(faqId, faqsDTO);
	}

	@Test
	void updateFaq_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int faqId = 1;
		when(faqsService.updateFaq(faqId, faqsDTO)).thenThrow(new NotFound("FAQ não encontrado",1));

		assertThrows(NotFound.class, () -> faqsController.updateFaq(faqId, faqsDTO));
		verify(faqsService, times(1)).updateFaq(faqId, faqsDTO);
	}

	@Test
	void deleteFaq_ValidId_CallsDeleteServiceMethod() throws NotFound {
		int faqId = 1;
		doNothing().when(faqsService).deleteFaq(faqId);

		faqsController.deleteFaq(faqId);

		verify(faqsService, times(1)).deleteFaq(faqId);
	}

	@Test
	void deleteFaq_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int faqId = 1;
		doThrow(new NotFound("FAQ não encontrado", 1)).when(faqsService).deleteFaq(faqId);

		assertThrows(NotFound.class, () -> faqsController.deleteFaq(faqId));
		verify(faqsService, times(1)).deleteFaq(faqId);
	}

	@Test
	void getAllFaqs_NoFilter_ReturnsFaqsPage() {
		List<FaqsDTO> faqsList = Collections.singletonList(faqsDTO);
		Page<FaqsDTO> faqsPage = new PageImpl<>(faqsList, pageable, faqsList.size());
		when(faqsService.getAllFaqs(faqsFilterDTO, pageable)).thenReturn(faqsPage);

		Page<FaqsDTO> resultPage = faqsController.getAllFaqs(faqsFilterDTO, pageable);

		assertEquals(faqsPage, resultPage);
		verify(faqsService, times(1)).getAllFaqs(faqsFilterDTO, pageable);
	}

	@Test
	void getFaqById_ValidId_ReturnsFaqsDTO() throws NotFound {
		int faqId = 1;
		when(faqsService.getFaqById(faqId)).thenReturn(faqsDTO);

		FaqsDTO foundFaq = faqsController.getFaqById(faqId);

		assertEquals(faqsDTO, foundFaq);
		verify(faqsService, times(1)).getFaqById(faqId);
	}

	@Test
	void getFaqById_NotFoundExceptionFromService_ThrowsNotFound() throws NotFound {
		int faqId = 1;
		when(faqsService.getFaqById(faqId)).thenThrow(new NotFound("FAQ não encontrado", 1));

		assertThrows(NotFound.class, () -> faqsController.getFaqById(faqId));
		verify(faqsService, times(1)).getFaqById(faqId);
	}
}