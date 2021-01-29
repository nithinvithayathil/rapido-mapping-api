package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import com.ustglobal.rapido.mapping.domain.shared.ValueObject;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.Semver.SemverType;

import java.util.Objects;

/**
 * Represents the unique identifier of a BusinessEntity. BusinessEntity is identified by a id and a semantic version.
 */
public class BusinessEntityId implements ValueObject<BusinessEntityId> {
    private String entityId;
    private String version;

    public BusinessEntityId(String entityId, String version) throws DomainException {
      DomainValidator.notNull(entityId, DomainErrorCode.PARAMETER_REQUIRED, "EntityId");
      DomainValidator.notNull(version, DomainErrorCode.PARAMETER_REQUIRED, "Version");
      this.entityId = entityId;
      this.version = version;
    }

    @Override
    public boolean sameValueAs(BusinessEntityId other) {
        return other != null && this.entityId.equals(other.entityId)
                && this.getVersion().isEqualTo(other.getVersion());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessEntityId that = (BusinessEntityId) o;
        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId, version);
    }


    public String getEntityId() {
        return entityId;
    }

    public Semver getVersion() {
        return new Semver(version, SemverType.LOOSE) ;
    }

  @Override
  public String toString() {
    return String.format("entityId: %s, version: %s", entityId, version);
  }
}
