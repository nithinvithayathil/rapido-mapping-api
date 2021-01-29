package com.ustglobal.rapido.mapping.domain.model.businessentity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
public class BusinessEntityUpdationServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@InjectMocks
	BusinessEntityUpdationService businessEntityUpdationService;

	@Mock
	private BusinessEntityRepository repository;

	@Captor
	private ArgumentCaptor<BusinessEntity> argumentCaptor;

	@Test
	public void updateBusinessEntityShouldThrowExceptionWhenBusinessEntityInNotPresentInDB() {
		String entityId = UUID.randomUUID().toString();
		String version = "0.0.1";
		expectedException.expect(DomainException.class);
		expectedException.expectMessage(
				"The requested business entity with id " + entityId + " and version " + version + " does not exist.");
		BusinessEntityId businessEntityId = new BusinessEntityId(entityId, version);
		when(repository.findByBusinessEntityId(businessEntityId)).thenReturn(Optional.ofNullable(null));
		businessEntityUpdationService.updateBusinessEntity(entityId, version, "BusinessEntityUpdatedName",
				"BusinessEntityUpdatedDescription");
		verify(repository).findByBusinessEntityId(businessEntityId);
	}

	@Test
	public void updateBusinessEntity() {
		String entityId = UUID.randomUUID().toString();
		BusinessEntityId businessEntityId = new BusinessEntityId(entityId, "0.0.1");
		BusinessEntity businessEntity = new BusinessEntity(businessEntityId, "BusinessEntityName",
				"BusinessEntityDescription");
		when(repository.findByBusinessEntityId(businessEntityId)).thenReturn(Optional.of(businessEntity));
		businessEntityUpdationService.updateBusinessEntity(entityId, "0.0.1", "BusinessEntityUpdatedName",
				"BusinessEntityUpdatedDescription");
		verify(repository).save(argumentCaptor.capture());
		verify(repository).findByBusinessEntityId(businessEntityId);
		assertNotNull(argumentCaptor.getValue());
		assertTrue(argumentCaptor.getValue().getId().equals(businessEntityId));
		assertTrue(argumentCaptor.getValue().getName().equals("BusinessEntityUpdatedName"));
		assertTrue(argumentCaptor.getValue().getDescription().equals("BusinessEntityUpdatedDescription"));
	}

}
