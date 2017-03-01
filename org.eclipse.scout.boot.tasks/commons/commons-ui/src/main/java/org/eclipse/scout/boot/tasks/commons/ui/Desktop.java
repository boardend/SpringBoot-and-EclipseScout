package org.eclipse.scout.boot.tasks.commons.ui;

import javax.inject.Inject;

import org.eclipse.scout.boot.tasks.commons.ui.admin.AdminOutline;
import org.eclipse.scout.boot.tasks.commons.ui.admin.ViewAdminOutlinePermission;
import org.eclipse.scout.boot.tasks.commons.ui.admin.user.OptionsForm;
import org.eclipse.scout.boot.tasks.commons.ui.task.TaskOutline;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.keystroke.IKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktopExtension;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.form.AbstractFormMenu;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.config.PlatformConfigProperties.ApplicationNameProperty;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;

/**
 * This Spring managed bean represents the web application.
 */
@Bean
public class Desktop extends AbstractDesktop {

  public static final String LOGO_ICON = "eclipse_scout";

  private final ApplicationNameProperty applicationNameConfig;

  @Inject
  public Desktop(final ApplicationNameProperty applicationNameConfig) {
    super(false);
    this.applicationNameConfig = applicationNameConfig;
    callInitializer();
  }

  @Override
  protected String getConfiguredTitle() {
    return applicationNameConfig.getValue();
  }

  @Override
  protected String getConfiguredLogoId() {
    return LOGO_ICON;
  }

  @Override
  protected void execDefaultView() {
    setOutline(TaskOutline.class);
  }

  @Order(10)
  public class HomeOutlineViewButton extends AbstractOutlineViewButton {

    public HomeOutlineViewButton() {
      this(TaskOutline.class);
    }

    protected HomeOutlineViewButton(final Class<? extends TaskOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected DisplayStyle getConfiguredDisplayStyle() {
      return DisplayStyle.TAB;
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F2;
    }
  }

  @Order(2000)
  public class AdminOutlineViewButton extends AbstractOutlineViewButton {

    public AdminOutlineViewButton() {
      this(AdminOutline.class);
    }

    protected AdminOutlineViewButton(Class<? extends AdminOutline> outlineClass) {
      super(Desktop.this, outlineClass);
    }

    @Override
    protected void execInitAction() {
      setVisiblePermission(new ViewAdminOutlinePermission());
    }

    @Override
    protected String getConfiguredIconId() {
      return FontAwesomeIcons.fa_users;
    }

    @Override
    protected DisplayStyle getConfiguredDisplayStyle() {
      return DisplayStyle.TAB;
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F3;
    }
  }

  @Order(1000)
  public class OptionsMenu extends AbstractFormMenu<OptionsForm> {

    @Override
    protected String getConfiguredIconId() {
      return FontAwesomeIcons.fa_cog;
    }

    @Override
    protected String getConfiguredKeyStroke() {
      return IKeyStroke.F10;
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("Options");
    }

    /**
     * Force a reload of the user data when the options form is shown again.
     */
    @Override
    protected void execSelectionChanged(boolean selected) {
      super.execSelectionChanged(selected);

      if (selected && getForm().isFormStarted()) {
        ((OptionsForm) getForm()).reload();
      }
    }

    @Override
    protected OptionsForm createForm() {
      return BEANS.get(OptionsForm.class);
    }
  }

  @Order(2000)
  public class FileMenu extends AbstractMenu {

    @Override
    protected String getConfiguredIconId() {
      return FontAwesomeIcons.fa_signOut;
    }

    @Override
    protected String getConfiguredTooltipText() {
      return TEXTS.get("Exit");
    }

    @Override
    protected void execAction() {
      ClientSessionProvider.currentSession(TasksClientSession.class).stop();
    }
  }

  public static class DesktopExtension extends AbstractDesktopExtension {

    @Override
    public void contributeOutlines(OrderedCollection<IOutline> outlines) {
      outlines.addAllLast(BEANS.all(IOutline.class));
    }
  }
}
