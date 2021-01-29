package com.ustglobal.rapido.mapping.domain.model.businessentity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.ustglobal.rapido.mapping.domain.shared.DomainException;

@RunWith(MockitoJUnitRunner.class)
public class BusinessEntityCreationServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	BusinessEntityCreationService businessEntityCreationService;

	@Mock
	private BusinessEntityRepository businessEntityRepository;

	@Captor
	private ArgumentCaptor<BusinessEntity> argumentCaptor;

	public static final String BUSINESS_ENTITY_NAME = "BusinessEntityName";

	public static final String BUSINESS_ENTITY_DESCRIPTION = "BusinessEntityDescription";

	@Test
	public void newBusinessEntityShouldThrowExceptionWhenEntityIdIsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("EntityId cannot be null or empty.");
		businessEntityCreationService.newBusinessEntity(null, BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
	}

	@Test
	public void newBusinessEntityShouldThrowExceptionWhenNameIsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Name cannot be null or empty.");
		businessEntityCreationService.newBusinessEntity(UUID.randomUUID().toString(), "", BUSINESS_ENTITY_DESCRIPTION);
	}

	@Test
	public void newBusinessEntity() {
		String id = UUID.randomUUID().toString();
		businessEntityCreationService.newBusinessEntity(id, BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		verify(businessEntityRepository).save(argumentCaptor.capture());
		assertNotNull(argumentCaptor.getValue());
		assertTrue(argumentCaptor.getValue().getId().getEntityId().equals(id));
		assertTrue(argumentCaptor.getValue().getName().equals(BUSINESS_ENTITY_NAME));
	}

}
