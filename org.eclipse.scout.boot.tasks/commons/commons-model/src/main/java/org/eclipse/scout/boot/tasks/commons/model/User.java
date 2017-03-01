package org.eclipse.scout.boot.tasks.commons.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class User extends Model<String> {

  public static final Locale LOCALE_DEFAULT = Locale.forLanguageTag("en-US");

  public static final int ID_LENGTH_MIN = 3;
  public static final int ID_LENGTH_MAX = 32;
  public static final String ID_PATTERN = "^[a-zA-Z0-9\\.]+$";
  public static final String ID_ERROR_LENGTH = "UserIdErrorLength";
  public static final String ID_ERROR_PATTERN = "UserIdErrorPattern";

  public static final int NAME_LENGTH_MIN = 1;
  public static final int NAME_LENGTH_MAX = 64;
  public static final String FIRST_NAME_ERROR_LENGTH = "FirstNameErrorLength";
  public static final String LAST_NAME_ERROR_LENGTH = "LastNameErrorLength";

  @Size(min = ID_LENGTH_MIN, max = ID_LENGTH_MAX, message = ID_ERROR_LENGTH)
  @Pattern(regexp = ID_PATTERN, message = ID_ERROR_PATTERN)
  @Override
  public String getId() {
    return super.getId();
  }

  @NotNull
  private String passwordHash;

  @NotNull
  private Locale locale;

  @NotNull
  @Size(min = NAME_LENGTH_MIN, max = NAME_LENGTH_MAX, message = FIRST_NAME_ERROR_LENGTH)
  private String firstName;

  @Size(max = NAME_LENGTH_MAX, message = LAST_NAME_ERROR_LENGTH)
  private String lastName;

  private UUID pictureId;
  private Set<String> roles = new HashSet<>();
  private boolean enabled; 

  public User() {
  } 

  public User(String userId, String firstName, String passwordHash) {
    super(userId);

    this.firstName = firstName;
    this.passwordHash = passwordHash;
    this.locale = LOCALE_DEFAULT;
    this.enabled = true;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String password) {
    this.passwordHash = password;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public UUID getPictureId() {
    return pictureId;
  }

  public void setPictureId(UUID pictureId) {
    this.pictureId = pictureId;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    this.roles = roles;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return id + "(" + toDisplayText() + ")";
  }

  public String toDisplayText() {
    if (lastName == null) {
      return firstName;
    }

    return String.join(" ", firstName, lastName);
  }

  public boolean isRoot() {
    if (roles == null) {
      return false;
    }

    return roles.contains(Role.ROOT_ID);
  }
}
