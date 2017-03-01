package org.eclipse.scout.boot.tasks.commons.ui.admin;

import java.util.List;

import org.eclipse.scout.boot.tasks.commons.ui.FontAwesomeIcons;
import org.eclipse.scout.boot.tasks.commons.ui.admin.db.DatabaseAdministrationConsolePage;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.PermissionTablePage;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleTablePage;
import org.eclipse.scout.boot.tasks.commons.ui.admin.user.UserTablePage;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.shared.TEXTS;

@Bean
public class AdminOutline extends AbstractOutline {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Administration");
  }

  @Override
  protected String getConfiguredIconId() {
    return FontAwesomeIcons.fa_users;
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) {
    pageList.add(BEANS.get(UserTablePage.class));
    pageList.add(BEANS.get(RoleTablePage.class));
    pageList.add(BEANS.get(PermissionTablePage.class));
    pageList.add(BEANS.get(DatabaseAdministrationConsolePage.class));
  }
}
