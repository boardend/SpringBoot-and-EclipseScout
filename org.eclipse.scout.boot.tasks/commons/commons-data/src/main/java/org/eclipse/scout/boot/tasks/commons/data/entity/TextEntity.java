package org.eclipse.scout.boot.tasks.commons.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.eclipse.scout.boot.tasks.commons.model.Text;

@Entity
public class TextEntity extends Text {

  @Id
  @Column(length = ID_LENGTH_MAX)
  @Override
  public String getId() {
    return super.getId();
  }

  @Column(length = TEXT_LENGTH_MAX)
  @Override
  public String getText() {
    return null;
  }
}
