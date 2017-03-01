package org.eclipse.scout.boot.tasks.commons.data.entity.converter;

import java.util.UUID;

import javax.persistence.AttributeConverter;

public class UuidConverter implements AttributeConverter<UUID, String> {

  @Override
  public String convertToDatabaseColumn(UUID uuid) {
    if (uuid == null) {
      return null;
    }

    return uuid.toString();
  }

  @Override
  public UUID convertToEntityAttribute(String uuid) {
    if (uuid == null) {
      return null;
    }

    return UUID.fromString(uuid);
  }

}
