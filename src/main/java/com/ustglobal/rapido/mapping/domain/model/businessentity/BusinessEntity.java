package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.shared.*;

import java.util.*;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldMapping;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

/**
 * Main entity class for modeling a business entity. A BusinessEntity represents
 * the canonical data model required in an enterprise data integration system.
 * It will have single or multiple sources and a single target. It will abstract
 * the mapping and transformation needs between source fields and target fields.
 */
public class BusinessEntity implements Entity<BusinessEntity>, Persistable {

	@Id
	private final BusinessEntityId businessEntityId;
	private String name;
	private String description;
	private List<BusinessEntityTag> tags;
	private Collection<DataSourceDefinition> sources;
	private DataSourceDefinition target;
	private Collection<DataFieldMapping> mappings;
	@Transient
	private boolean hasChanges = false;

	public BusinessEntity(final BusinessEntityId businessEntityId, final String name, final String description)
      throws DomainException {
    DomainValidator.notNull(businessEntityId, DomainErrorCode.PARAMETER_REQUIRED, "BusinessEntityId");
    DomainValidator.notEmpty(name, DomainErrorCode.PARAMETER_REQUIRED, "Name");
		this.businessEntityId = businessEntityId;
		this.name = name;
		this.description = description;
	}

	public void addSource(DataSourceDefinition source) throws DomainException {
    DomainValidator.notNull(source, DomainErrorCode.PARAMETER_REQUIRED, "Source");
		if (this.sources == null) {
			sources = new ArrayList<>();
		}
		sources.add(source);
		hasChanges = true;
	}

	public void removeSource(DataSourceDefinition source) throws DomainException {
    DomainValidator.notNull(source, DomainErrorCode.PARAMETER_REQUIRED, "Source");
		if (this.sources != null) {
			sources.remove(source);
			hasChanges = true;
		}
	}

	public void removeSourceById(String sourceId) {
		DataSourceDefinition existingDataSourceDefinition = findDataSourceDefinitionById(sourceId);
		if (!ObjectUtils.isEmpty(this.mappings)) {
			this.mappings.forEach(mapping -> {
				boolean isMappingAvailableForSource = mapping.isSourceMapped(sourceId);
				if (isMappingAvailableForSource) {
					throw new DomainException(DomainErrorCode.SOURCE_FIELD_ALREADY_MAPPED, null,
							mapping.getFieldMappingId());
				}
			});
		}
		if (existingDataSourceDefinition != null) {
			removeSource(existingDataSourceDefinition);
		}
	}

	public DataSourceDefinition findDataSourceDefinitionById(String id) throws DomainException {
    DomainValidator.notEmpty(id, DomainErrorCode.PARAMETER_REQUIRED, "Id");
    if(this.sources == null){
      return null;
    }
		Optional<DataSourceDefinition> dataSourceDefinition = this.sources.stream()
				.filter(source -> source.getSourceDefinitionId().equals(id)).findFirst();
		return dataSourceDefinition.orElse(null);
	}

	public void addMapping(DataFieldMapping mapping) throws DomainException {
    DomainValidator.notNull(mapping, DomainErrorCode.PARAMETER_REQUIRED, "DataFieldMapping");
		if (this.mappings == null) {
			mappings = new ArrayList<>();
		}
		if(!mapping.isDefault()) {
			validateSourceFieldsExist(mapping.getSourceFieldIds());
		}
		validateTargetFieldExist(mapping.getTargetFieldId());
		validateIfTargetAlreadyMapped(mapping.getTargetFieldId());
		mappings.add(mapping);
		hasChanges = true;
	}

  public void removeMapping(DataFieldMapping mapping) throws DomainException {
    DomainValidator.notNull(mapping, DomainErrorCode.PARAMETER_REQUIRED, "DataFieldMapping");
		if (this.mappings != null) {
			mappings.remove(mapping);
			hasChanges = true;
		}
	}

	public void removeMappingById(String mappingId){
    DomainValidator.notEmpty(mappingId, DomainErrorCode.PARAMETER_REQUIRED, "MappingIds");
	  DataFieldMapping existingMapping = findDataFieldMappingById(mappingId);
	  if(existingMapping != null){
	    removeMapping(existingMapping);
    }
  }

    public void removeSourceFieldFromMapping(String mappingId, FieldDefinitionId sourceFieldId){
        DomainValidator.notEmpty(mappingId, DomainErrorCode.PARAMETER_REQUIRED, "MappingId");
        DomainValidator.notNull(sourceFieldId, DomainErrorCode.PARAMETER_REQUIRED, "SourceFieldId");
        DataFieldMapping fieldMapping = findDataFieldMappingById(mappingId);
        fieldMapping.removeSourceField(sourceFieldId);
        if(CollectionUtils.isEmpty(fieldMapping.getSourceFieldIds())){
            removeMapping(fieldMapping);
        }
    }

	public DataFieldMapping findDataFieldMappingById(String id) throws DomainException {
    DomainValidator.notEmpty(id, DomainErrorCode.PARAMETER_REQUIRED, "Id");
    if(this.mappings == null){
      return null;
    }
		Optional<DataFieldMapping> dataFieldMapping = this.mappings.stream()
				.filter(mapping -> mapping.getFieldMappingId().equals(id)).findFirst();
		return dataFieldMapping.orElse(null);
	}

	public void removeAllMapping() {
		mappings = new ArrayList<>();
		hasChanges = true;
	}

	public void assignTarget(DataSourceDefinition target) throws DomainException {
    DomainValidator.notNull(target, DomainErrorCode.PARAMETER_REQUIRED, "Target");
		this.target = target;
		hasChanges = true;
	}

	public void removeTarget() {
		if (!CollectionUtils.isEmpty(this.mappings)) {
			throw new DomainException(DomainErrorCode.MAPPING_EXIST, null);
		}
		this.target = null;
		hasChanges = true;
	}

	/**
	 * Change the name of business entity
	 * 
	 * @param newName
	 *            New name to be applied
	 */
	public void changeName(String newName) throws DomainException {
    DomainValidator.notEmpty(newName, DomainErrorCode.PARAMETER_REQUIRED, "Name");
		if (!newName.equals(this.name)) {
			this.name = newName;
			hasChanges = true;
		}
	}

	/**
	 * Change the description of business entity
	 * 
	 * @param newDescription
	 *            New description to be applied
	 */
	public void changeDescription(String newDescription) throws DomainException {
    DomainValidator.notEmpty(newDescription, DomainErrorCode.PARAMETER_REQUIRED, "Description");
		if (!newDescription.equals(this.description)) {
			this.description = newDescription;
			hasChanges = true;
		}
	}

  /**
   * Creates a cloned copy of this business entity with provided version
   * @param newVersion Version for new entity
   * @return New entity created
   */
	public BusinessEntity cloneWithNewVersion(String newVersion){
	  BusinessEntityId newId = new BusinessEntityId(businessEntityId.getEntityId(), newVersion);
	  BusinessEntity newEntity = new BusinessEntity(newId, name, description);
    if(!CollectionUtils.isEmpty(this.sources)) {
      this.sources.forEach(newEntity::addSource);
    }
    if(target != null) {
      newEntity.assignTarget(target);
    }
    if(!CollectionUtils.isEmpty(this.mappings)) {
      this.mappings.forEach(newEntity::addMapping);
    }
	  return newEntity;
  }

  public boolean isLocked(){
    return checkForTag(BusinessEntityTag.LOCKED);
  }

  public boolean isBaseline(){
    return checkForTag(BusinessEntityTag.BASELINE);
  }

  public boolean isLatest(){
    return checkForTag(BusinessEntityTag.LATEST);
  }

  private boolean checkForTag(BusinessEntityTag tag) {
    return !CollectionUtils.isEmpty(this.tags) && this.tags.contains(tag);
  }

  void addTag(BusinessEntityTag tag){
    createTagsIfNotExisting();
    if(!this.tags.contains(tag)) {
      this.tags.add(tag);
      hasChanges = true;
    }
  }

  void removeTag(BusinessEntityTag tag){
	  if(!CollectionUtils.isEmpty(this.tags) && this.tags.contains(tag)) {
      this.tags.remove(tag);
      hasChanges = true;
    }
  }

  private void createTagsIfNotExisting() {
    if(ObjectUtils.isEmpty(this.tags)){
      this.tags = new ArrayList<>();
    }
  }

  @Override
	public boolean sameIdentityAs(BusinessEntity other) {
		return other != null && this.businessEntityId.sameValueAs(other.businessEntityId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BusinessEntity that = (BusinessEntity) o;

		return sameIdentityAs(that);
	}

	@Override
	public int hashCode() {
		return Objects.hash(businessEntityId);
	}

	public BusinessEntityId getId() {
		return this.businessEntityId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Collection<DataSourceDefinition> getSources() {
		if(sources == null){
			return null;
		}
		return Collections.unmodifiableCollection(sources);
	}

	public DataSourceDefinition getTarget() {
		return target;
	}

	public Collection<DataFieldMapping> getMappings() {
		if(mappings == null){
			return null;
		}
		return Collections.unmodifiableCollection(mappings);
	}

	/**
	 * Indicates if there are changes to the model, that are not persisted.
	 * 
	 * @return True if there are non persisted changes.
	 */
	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges || hasChangesInSources()
				|| hasChangesInTarget() || hasChangesInMappings();
	}

	private boolean hasChangesInTarget() {
		if(this.target == null){
			return false;
		}
		return this.target.hasNonPersistedChanges();
	}

	private boolean hasChangesInSources() {
		if(this.sources == null){
			return false;
		}
		return this.sources.stream().anyMatch(DataSourceDefinition::hasNonPersistedChanges);
	}

  private boolean hasChangesInMappings() {
    if(this.mappings == null){
      return false;
    }
    return this.mappings.stream().anyMatch(DataFieldMapping::hasNonPersistedChanges);
  }


  private void validateTargetFieldExist(FieldDefinitionId targetFieldId) {
	  DomainValidator.notNull(targetFieldId, DomainErrorCode.PARAMETER_REQUIRED, "TargetFieldId");
	  validateTargetExist(targetFieldId.getSourceDefinitionId());
    DataFieldDefinition dataFieldDefinition = this.target.findDataFieldDefinitionById(targetFieldId);

    if(ObjectUtils.isEmpty(dataFieldDefinition)){
      throw new DomainException(DomainErrorCode.FIELD_NOT_EXISTING_IN_TARGET, null, targetFieldId.getId(), targetFieldId.getSourceDefinitionId());
    }
  }

  private void validateTargetExist(String dataSourceDefinitionId) {
    if(ObjectUtils.isEmpty(this.target) ||
        !this.target.getSourceDefinitionId().equals(dataSourceDefinitionId)){
      throw new DomainException(DomainErrorCode.TARGET_DOES_NOT_EXIST, null, dataSourceDefinitionId, this.businessEntityId);
    }
  }

  private void validateIfTargetAlreadyMapped(FieldDefinitionId targetFieldId){
    DomainValidator.notNull(targetFieldId, DomainErrorCode.PARAMETER_REQUIRED, "TargetFieldId");
    DataFieldMapping mappingFoundForTarget = findMappingByTargetFieldId(targetFieldId);
    if(!ObjectUtils.isEmpty(mappingFoundForTarget)){
      throw new DomainException(DomainErrorCode.TARGET_FIELD_ALREADY_MAPPED, null, targetFieldId, mappingFoundForTarget.getFieldMappingId());
    }
  }

  private DataFieldMapping findMappingByTargetFieldId(FieldDefinitionId targetFieldId){
    for (DataFieldMapping mapping : this.mappings) {
      if (mapping.getTargetFieldId().equals(targetFieldId)) {
        return mapping;
      }
    }
    return null;
  }

  private void validateSourceFieldsExist(Collection<FieldDefinitionId> sourceFieldIds) {
    DomainValidator.notEmpty(sourceFieldIds, DomainErrorCode.PARAMETER_REQUIRED, "SourceFieldIds");
    sourceFieldIds.forEach(sourceFieldId -> {
      DataSourceDefinition dataSourceDefinition = this.findDataSourceDefinitionById(sourceFieldId.getSourceDefinitionId());
      if(ObjectUtils.isEmpty(dataSourceDefinition)){
       throw new DomainException(DomainErrorCode.SOURCE_DOES_NOT_EXIST, null, sourceFieldId.getSourceDefinitionId(), businessEntityId);
      }

      DataFieldDefinition dataFieldDefinition = dataSourceDefinition.findDataFieldDefinitionById(sourceFieldId);
      if(ObjectUtils.isEmpty(dataFieldDefinition)){
        throw new DomainException(DomainErrorCode.FIELD_NOT_EXISTING_IN_SOURCE, null, sourceFieldId.getId(), sourceFieldId.getSourceDefinitionId());
      }
    });
  }

}
