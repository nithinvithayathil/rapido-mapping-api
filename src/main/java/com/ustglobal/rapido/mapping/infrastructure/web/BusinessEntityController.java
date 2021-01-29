package com.ustglobal.rapido.mapping.infrastructure.web;

import com.ustglobal.rapido.mapping.application.ApplicationServiceRegistry;
import com.ustglobal.rapido.mapping.domain.DomainServiceRegistry;
import com.ustglobal.rapido.mapping.domain.model.businessentity.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldMapping;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinitionType;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldTransformation;
import com.ustglobal.rapido.mapping.domain.model.businessentity.transformation.Transformation;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.ustglobal.rapido.mapping.infrastructure.web.exceptions.*;
import com.ustglobal.rapido.mapping.infrastructure.web.mappers.Mapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/businessentities")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BusinessEntityController extends GenericControllerAdvice {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessEntityController.class);

	@Autowired
	private BusinessEntityRepository repository;

	@Autowired
	private EntityVersionGroupRepository versionGroupRepository;

	@Autowired
	private Mapper<BusinessEntityPreview, BusinessEntity> businessEntityMapper;

	@Autowired
	private ApplicationServiceRegistry applicationServiceRegistry;

	@Autowired
	private DomainServiceRegistry domainServiceRegistry;

	@GetMapping
	public List<BusinessEntityPreview> getAllBusinessEntities() {
		LOG.debug("Entering getAllBusinessEntities");

		List<BusinessEntityPreview> businessEntityPreviews = businessEntityMapper.mapListFrom(repository.findAll());

		LOG.debug("Leaving getAllBusinessEntities");
		return businessEntityPreviews;
	}

	@GetMapping("/latest")
	public List<BusinessEntityPreview> getAllLatestVersionOfBusinessEntities() {
		LOG.debug("Entering getAllLatestVersionOfBusinessEntities");

		List<BusinessEntityPreview> businessEntityPreviews = businessEntityMapper
				.mapListFrom(repository.findAllLatestVersions());

		LOG.debug("Leaving getAllLatestVersionOfBusinessEntities");
		return businessEntityPreviews;
	}

	@GetMapping("/{entityId}")
	public List<BusinessEntityPreview> getBusinessEntitiesById(@PathVariable("entityId") String entityId) {
		LOG.debug("Entering getBusinessEntitiesById");

		List<BusinessEntityPreview> businessEntityPreviews = businessEntityMapper
				.mapListFrom(repository.findBusinessEntitiesWithId(entityId));

		LOG.debug("Leaving getBusinessEntitiesById");
		return businessEntityPreviews;
	}

	@GetMapping("/{entityId}/{version}")
	public BusinessEntityPreview getBusinessEntity(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) throws ResourceNotFoundException, DomainException {
		LOG.debug("Entering getBusinessEntity");

		BusinessEntityId businessEntityId = new BusinessEntityId(entityId, version);
		Optional<BusinessEntity> businessEntity = repository.findByBusinessEntityId(businessEntityId);
		if (!businessEntity.isPresent()) {
			throw new ResourceNotFoundException("BusinessEntity", businessEntity);
		}
		BusinessEntityPreview businessEntityPreview = businessEntityMapper.mapFrom(businessEntity.get());

		LOG.debug("Leaving getBusinessEntity");
		return businessEntityPreview;

	}

	@PostMapping
	public ResponseEntity<BusinessEntityPreview> createBusinessEntity(
			@RequestBody SaveEntityCommand saveEntityCommand) {
		LOG.debug("Entering createBusinessEntity");
		BusinessEntity newBusinessEntity = applicationServiceRegistry.getBusinessEntityCreationService()
				.newBusinessEntity(UUID.randomUUID().toString(), saveEntityCommand.getName(),
						saveEntityCommand.getDescription());
		BusinessEntityPreview entityPreview = businessEntityMapper.mapFrom(newBusinessEntity);
		LOG.debug("Leaving createBusinessEntity");
		return new ResponseEntity<BusinessEntityPreview>(entityPreview, HttpStatus.CREATED);
	}

	@PutMapping("/{entityId}/{version}")
	public ResponseEntity<BusinessEntityPreview> updateBusinessEntity(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestBody SaveEntityCommand saveEntityCommand) {
		LOG.debug("Entering updateBusinessEntity");

		BusinessEntity businessEntity = domainServiceRegistry.getEntityUpdationService().updateBusinessEntity(entityId,
				version, saveEntityCommand.getName(), saveEntityCommand.getDescription());
		LOG.debug("Leaving updateBusinessEntity");
		return new ResponseEntity<>(businessEntityMapper.mapFrom(businessEntity), HttpStatus.OK);
	}

	@PutMapping("/{entityId}/{version}/baseline")
	public ResponseEntity<BusinessEntityPreview> baselineBusinessEntity(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) {
		LOG.debug("Entering baselineBusinessEntity");
		EntityVersionGroup entityVersionGroup = versionGroupRepository.findById(entityId);
		entityVersionGroup.markAsBaseline(version);
		entityVersionGroup = versionGroupRepository.save(entityVersionGroup);
		BusinessEntity businessEntity = entityVersionGroup.findEntityWithVersion(version);
		LOG.debug("Leaving baselineBusinessEntity");
		return new ResponseEntity<>(businessEntityMapper.mapFrom(businessEntity), HttpStatus.OK);
	}

	@PutMapping("/{entityId}/{version}/newversion")
	public ResponseEntity<BusinessEntityPreview> newVersionOfBusinessEntity(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestBody() NewVersionCommand newVersionCommand) {
		LOG.debug("Entering newVersionOfBusinessEntity");
		EntityVersionGroup entityVersionGroup = versionGroupRepository.findById(entityId);
		BusinessEntity createdEntity = entityVersionGroup.createNewVersionOf(version,
				newVersionCommand.getSuggestedVersion());
		entityVersionGroup = versionGroupRepository.save(entityVersionGroup);
		BusinessEntity businessEntity = entityVersionGroup
				.findEntityWithVersion(createdEntity.getId().getVersion().toString());
		LOG.debug("Leaving newVersionOfBusinessEntity");
		return new ResponseEntity<>(businessEntityMapper.mapFrom(businessEntity), HttpStatus.OK);
	}

	@DeleteMapping("/{entityId}/{version}")
	public void deleteBusinessEntity(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) {
		LOG.debug("Entering deleteBusinessEntity");
		repository.deleteById(new BusinessEntityId(entityId, version));
		LOG.debug("Leaving deleteBusinessEntity");

	}

	@DeleteMapping("/{entityId}")
	public void deleteAllBusinessEntitiesById(@PathVariable("entityId") String entityId) {
		LOG.debug("Entering deleteAllBusinessEntitiesById");
		List<BusinessEntity> businessEntitiesToDelete = repository.findBusinessEntitiesWithId(entityId);
		if (!CollectionUtils.isEmpty(businessEntitiesToDelete)) {
			businessEntitiesToDelete.forEach(businessEntity -> {
				repository.deleteById(businessEntity.getId());
			});
		}

		LOG.debug("Leaving deleteAllBusinessEntitiesById");

	}

	@GetMapping("/{entityId}/{version}/sources")
	public Collection<DataSourceDefinition> getSources(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) {
		LOG.debug("Entering getSources");

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);

		LOG.debug("Leaving getSources");
		return businessEntity.getSources() == null ? new ArrayList<>() : businessEntity.getSources();
	}

	@GetMapping("/{entityId}/{version}/sources/{sourceId}")
	public ResponseEntity<DataSourceDefinition> getSourceById(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("sourceId") String sourceId) {
		LOG.debug("Entering getSourceById");

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);

		DataSourceDefinition dataSourceDefinition = businessEntity.findDataSourceDefinitionById(sourceId);

		if (dataSourceDefinition == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		LOG.debug("Leaving getSourceById");

		return new ResponseEntity<>(dataSourceDefinition, HttpStatus.OK);
	}

	@PostMapping("/{entityId}/{version}/sources/import/fromfiles")
	public ResponseEntity importSourceDataSourceDefinition(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestPart("file") MultipartFile[] files,
			@RequestPart("importFromFilesCommand") ImportDataSourceFromFilesCommand command) {

		LOG.debug(
				"Entering importSourceDataSourceDefinition. EntityId: {}, Version: {} DefinitionType: {}, Number of files: {}",
				entityId, version, command.getDataSourceDefinitionType(), (files == null) ? 0 : files.length);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataSourceDefinition dataSourceDefinition = importDataSourceDefinitionFromFiles(files, command);
		businessEntity.addSource(dataSourceDefinition);
		persist(businessEntity);

		LOG.debug("Leaving importSourceDataSourceDefinition");

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@PostMapping("/{entityId}/{version}/sources/import/fromdb")
	public ResponseEntity importSourceDataSourceDefinitionFromDB(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestBody ImportDataSourceFromDbCommand command) {

		LOG.debug("Entering importSourceDataSourceDefinitionFromDB. EntityId: {}, Version: {} command: {}", entityId,
				version, command);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataSourceDefinition dataSourceDefinition = importDataSourceDefinitionFromDb(command);
		businessEntity.addSource(dataSourceDefinition);
		persist(businessEntity);

		LOG.debug("Leaving importSourceDataSourceDefinitionFromDB");

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@DeleteMapping("/{entityId}/{version}/sources/{sourceId}")
	public void deleteSourceById(@PathVariable("entityId") String entityId, @PathVariable("version") String version,
			@PathVariable("sourceId") String sourceId) {
		LOG.debug("Entering deleteSourceById");

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		businessEntity.removeSourceById(sourceId);

		persist(businessEntity);

		LOG.debug("Leaving deleteSourceById");

	}

	@GetMapping("/{entityId}/{version}/target")
	public ResponseEntity<DataSourceDefinition> getTarget(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) {
		LOG.debug("Entering getTarget");

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		if (businessEntity.getTarget() == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		LOG.debug("Leaving getTarget");

		return new ResponseEntity<>(businessEntity.getTarget(), HttpStatus.OK);
	}

	@PostMapping("/{entityId}/{version}/target/import/fromfiles")
	public ResponseEntity importTargetDataSourceDefinition(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestPart("file") MultipartFile[] files,
			@RequestPart("importFromFilesCommand") ImportDataSourceFromFilesCommand command) {

		LOG.debug(
				"Entering importTargetDataSourceDefinition. EntityId: {}, Version: {} DefinitionType: {}, Number of files: {}",
				entityId, version, command.getDataSourceDefinitionType(), (files == null) ? 0 : files.length);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataSourceDefinition dataSourceDefinition = importDataSourceDefinitionFromFiles(files, command);
		businessEntity.assignTarget(dataSourceDefinition);
		persist(businessEntity);

		LOG.debug("Leaving importTargetDataSourceDefinition");
		return new ResponseEntity(HttpStatus.CREATED);
	}

	@PostMapping("/{entityId}/{version}/target/import/fromdb")
	public ResponseEntity importTargetDataSourceDefinitionFromDb(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestBody ImportDataSourceFromDbCommand command) {

		LOG.debug("Entering importTargetDataSourceDefinitionFromDb. EntityId: {}, Version: {}, command : {}", entityId,
				version, command);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataSourceDefinition dataSourceDefinition = importDataSourceDefinitionFromDb(command);
		businessEntity.assignTarget(dataSourceDefinition);
		persist(businessEntity);

		LOG.debug("Leaving importTargetDataSourceDefinitionFromDb");

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@DeleteMapping("/{entityId}/{version}/target")
	public void deleteTarget(@PathVariable("entityId") String entityId, @PathVariable("version") String version) {
		LOG.debug("Entering deleteTarget for Entityid {}, verson: {}", entityId, version);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		businessEntity.removeTarget();
		persist(businessEntity);

		LOG.debug("Leaving deleteTarget");
	}

	@GetMapping("/{entityId}/{version}/mappings")
	public Collection<DataFieldMapping> getMappings(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version) {
		LOG.debug("Entering getMappings for Entityid {}, verson: {}", entityId, version);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);

		LOG.debug("Leaving getMappings");
		return businessEntity.getMappings() == null ? new ArrayList<>() : businessEntity.getMappings();
	}

	@GetMapping("/{entityId}/{version}/mappings/{mappingId}")
	public ResponseEntity<DataFieldMapping> getMappingById(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId) {

		LOG.debug("Entering getMappingById for Entityid {}, verson: {}, mappingId: {}", entityId, version, mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		if (fieldMapping == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		LOG.debug("Leaving getMappingById");

		return new ResponseEntity<>(fieldMapping, HttpStatus.OK);
	}

	@PostMapping("/{entityId}/{version}/mappings")
	public ResponseEntity<DataFieldMapping> createMapping(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @RequestBody SaveMappingCommand saveMappingCommand) {

		LOG.debug("Entering createMapping for Entityid {}, verson: {}", entityId, version);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		String mappingId = UUID.randomUUID().toString();
		businessEntity.addMapping(new DataFieldMapping(mappingId, saveMappingCommand.getTarget(),
				saveMappingCommand.getSources(), saveMappingCommand.getIsDefault()));

		businessEntity = persist(businessEntity);

		LOG.debug("Leaving createMapping");

		return new ResponseEntity<>(businessEntity.findDataFieldMappingById(mappingId), HttpStatus.CREATED);
	}

	@PostMapping("/{entityId}/{version}/mappings/{mappingId}/sourcefields")
	public ResponseEntity<DataFieldMapping> addSourceFieldsToMapping(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@RequestBody FieldDefinitionId sourceFieldToAdd) {

		LOG.debug("Entering createMapping for Entityid {}, verson: {}", entityId, version);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		fieldMapping.addSourceFields(sourceFieldToAdd);

		businessEntity = persist(businessEntity);

		LOG.debug("Leaving createMapping");

		return new ResponseEntity<>(businessEntity.findDataFieldMappingById(mappingId), HttpStatus.CREATED);
	}

	@DeleteMapping("/{entityId}/{version}/mappings/{mappingId}/sourcefields/{sourceId}/{fieldId}")
	public void removeSourceFieldsFromMapping(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@PathVariable("sourceId") String sourceId, @PathVariable("fieldId") String fieldId) {

		LOG.debug(
				"Entering removeSourceFieldsFromMapping for Entityid {}, verson: {}, mappingId: {}, sourceId: {}, fieldId: {}",
				entityId, version, mappingId, sourceId, fieldId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		businessEntity.removeSourceFieldFromMapping(mappingId, new FieldDefinitionId(fieldId, sourceId));
		persist(businessEntity);

		LOG.debug("Leaving removeSourceFieldsFromMapping");
	}

	@PostMapping("/{entityId}/{version}/mappings/{mappingId}/sourcetransforms")
	public ResponseEntity<FieldTransformation> addSourceTransform(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@RequestBody FieldTransformation fieldTransformation) {
		LOG.debug("Entering addSourceTransform for Entityid {}, verson: {}, mappingId: {}", entityId, version,
				mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		checkIfFieldMapExist(mappingId, businessEntity, fieldMapping);
		fieldMapping.addSourceTransformations(fieldTransformation);

		persist(businessEntity);

		LOG.debug("Leaving addSourceTransform");
		return new ResponseEntity<>(fieldTransformation, HttpStatus.CREATED);
	}

	@PutMapping("/{entityId}/{version}/mappings/{mappingId}/sourcetransforms")
	public ResponseEntity<FieldTransformation> updateSourceTransform(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@RequestBody FieldTransformation fieldTransformation) {
		LOG.debug("Entering addSourceTransform for Entityid {}, verson: {}, mappingId: {}", entityId, version,
				mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		checkIfFieldMapExist(mappingId, businessEntity, fieldMapping);
		fieldMapping.removeSourceTransformationByFieldId(fieldTransformation.getFieldDefinitionId());
		fieldMapping.addSourceTransformations(fieldTransformation);

		persist(businessEntity);

		LOG.debug("Leaving addSourceTransform");
		return new ResponseEntity<>(fieldTransformation, HttpStatus.OK);
	}

	@DeleteMapping("/{entityId}/{version}/mappings/{mappingId}/sourcetransforms")
	public void deleteSourceTransform(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@RequestBody FieldDefinitionId fieldDefinitionId) {
		LOG.debug("Entering addSourceTransform for Entityid {}, verson: {}, mappingId: {}, {}", entityId, version,
				mappingId, fieldDefinitionId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		checkIfFieldMapExist(mappingId, businessEntity, fieldMapping);
		fieldMapping.removeSourceTransformationByFieldId(fieldDefinitionId);

		persist(businessEntity);

		LOG.debug("Leaving deleteSourceTransform");
	}

	@DeleteMapping("/{entityId}/{version}/mappings/{mappingId}")
	public void deleteMapping(@PathVariable("entityId") String entityId, @PathVariable("version") String version,
			@PathVariable("mappingId") String mappingId) {
		LOG.debug("Entering deleteMapping for Entityid {}, verson: {}, mappingId: {}", entityId, version, mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		businessEntity.removeMappingById(mappingId);

		persist(businessEntity);

		LOG.debug("Leaving deleteMapping");
	}

	@PostMapping("/{entityId}/{version}/mappings/{mappingId}/combinetransform")
	public ResponseEntity<Transformation> assignCombineTransform(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId,
			@RequestBody Transformation transformation) {
		LOG.debug("Entering assignCombineTransform for Entityid {}, verson: {}, mappingId: {}", entityId, version,
				mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		checkIfFieldMapExist(mappingId, businessEntity, fieldMapping);
		fieldMapping.assignCombineTransform(transformation);

		persist(businessEntity);

		LOG.debug("Leaving assignCombineTransform");
		return new ResponseEntity<>(transformation, HttpStatus.CREATED);
	}

	@DeleteMapping("/{entityId}/{version}/mappings/{mappingId}/combinetransform")
	public void deleteCombineTransform(@PathVariable("entityId") String entityId,
			@PathVariable("version") String version, @PathVariable("mappingId") String mappingId) {
		LOG.debug("Entering deleteCombineTransform for Entityid {}, verson: {}, mappingId: {}, {}", entityId, version,
				mappingId);

		BusinessEntity businessEntity = fetchBusinessEntityForAction(entityId, version);
		DataFieldMapping fieldMapping = businessEntity.findDataFieldMappingById(mappingId);
		checkIfFieldMapExist(mappingId, businessEntity, fieldMapping);
		fieldMapping.removeCombineTransform();

		persist(businessEntity);

		LOG.debug("Leaving deleteCombineTransform");
	}

	private void checkIfFieldMapExist(@PathVariable("mappingId") String mappingId, BusinessEntity businessEntity,
			DataFieldMapping fieldMapping) {
		if (fieldMapping == null) {
			throw new DomainException(DomainErrorCode.MAPPING_DOES_NOT_EXIST, null, mappingId, businessEntity.getId());
		}
	}

	private DataSourceDefinition importDataSourceDefinitionFromDb(ImportDataSourceFromDbCommand command) {
		return applicationServiceRegistry.getImportDataSourceDefinitionFromDatabaseService().importFromTableFields(
				command.getDataSourceName(), command.getDbConfigId(), command.getSchema(), command.getTables());
	}

	private BusinessEntity fetchBusinessEntityForAction(String entityId, String version) {
		BusinessEntityId businessEntityId = new BusinessEntityId(entityId, version);
		Optional<BusinessEntity> businessEntity = repository.findByBusinessEntityId(businessEntityId);

		if (!businessEntity.isPresent()) {
			throw new DomainException(DomainErrorCode.BUSINESSENTITY_DOES_NOT_EXIST, null, entityId, version);
		}
		return businessEntity.get();
	}

	private BusinessEntity persist(BusinessEntity businessEntity) {
		if (businessEntity.hasNonPersistedChanges()) {
			return repository.save(businessEntity);
		}

		return businessEntity;
	}

	private DataSourceDefinition importXSDDataSourceDefinition(MultipartFile[] files,
			ImportDataSourceFromFilesCommand command) {
		return applicationServiceRegistry.getImportDataSourceFromXSDService()
				.importDatasourceFromXSDFiles(command.getMainFileName(), files, command.getDataSourceName());
	}

	private boolean isXSDImportRequest(String definitionType) {
		return DataSourceDefinitionType.getByTextValue(definitionType).equals(DataSourceDefinitionType.XSD);
	}

	private DataSourceDefinition importDataSourceDefinitionFromFiles(MultipartFile[] files,
			ImportDataSourceFromFilesCommand command) {
		DataSourceDefinition dataSourceDefinition = null;

		if (isXSDImportRequest(command.getDataSourceDefinitionType())) {
			dataSourceDefinition = importXSDDataSourceDefinition(files, command);
		}
		return dataSourceDefinition;
	}
}
