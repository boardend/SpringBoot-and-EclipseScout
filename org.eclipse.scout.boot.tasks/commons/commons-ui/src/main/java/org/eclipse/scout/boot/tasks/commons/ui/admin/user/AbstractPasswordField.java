package org.eclipse.scout.boot.tasks.commons.ui.admin.user;

import javax.inject.Inject;

import org.eclipse.scout.boot.tasks.commons.model.service.PasswordService;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.status.MultiStatus;
import org.eclipse.scout.rt.platform.status.Status;
import org.eclipse.scout.rt.shared.TEXTS;

public class AbstractPasswordField extends AbstractStringField {

  @Inject
  private PasswordService passwordService;

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("Password");
  }

  @Override
  protected boolean getConfiguredInputMasked() {
    return true;
  }

  /**
   * Verifies password value and sets error indicators accordingly.
   *
   * @return true if the password complies with the implemented password policy. in all other cases false is returned.
   */
  public boolean validateField() {
    clearErrorStatus();

    if (getValue() == null) {
      setError(TEXTS.get("PasswordEmptyError"));
      return false;
    }

    if (getValue().length() < PasswordService.PASSWORD_LENGTH_MIN) {
      setError(TEXTS.get("PasswordTooShortError", String.valueOf(PasswordService.PASSWORD_LENGTH_MIN)));
      return false;
    }

    if (getValue().length() > PasswordService.PASSWORD_LENGTH_MAX) {
      setError(TEXTS.get("PasswordTooLongError", String.valueOf(PasswordService.PASSWORD_LENGTH_MAX)));
      return false;
    }

    // make sure that we catch all relevant password constrains
    if (passwordService.matchesPasswordPolicy(getValue())) {
      setError(TEXTS.get("PasswordPolicyError"));
      return false;
    }

    return true;
  }

  public void setError(String message) {
    MultiStatus error = new MultiStatus();
    Status status = new Status(message);
    error.add(status);
    setErrorStatus(error);
  }
}
