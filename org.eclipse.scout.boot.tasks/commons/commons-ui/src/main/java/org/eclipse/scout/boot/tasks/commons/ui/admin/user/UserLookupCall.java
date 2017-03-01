package org.eclipse.scout.boot.tasks.commons.ui.admin.user;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.scout.boot.tasks.commons.model.User;
import org.eclipse.scout.boot.tasks.commons.model.service.UserService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class UserLookupCall extends LocalLookupCall<String> {

  @Inject
  UserService userService;

  private static final long serialVersionUID = 1L;

  @Override
  protected List<? extends ILookupRow<String>> execCreateLookupRows() {
    List<ILookupRow<String>> list = new ArrayList<>();

    for (User user : userService.getAll()) {
      list.add(new LookupRow<>(user.getId(), user.toDisplayText()));
    }

    return list;
  }
}
