package org.eclipse.scout.boot.tasks.commons.ui.admin.role;

import java.security.Permission;
import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.scout.boot.tasks.commons.model.Role;
import org.eclipse.scout.boot.tasks.commons.model.service.RoleService;
import org.eclipse.scout.boot.tasks.commons.ui.AbstractDirtyFormHandler;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleForm.MainBox.CancelButton;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleForm.MainBox.OkButton;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleForm.MainBox.RoleBox;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleForm.MainBox.RoleBox.PermissionTableField;
import org.eclipse.scout.boot.tasks.commons.ui.admin.role.RoleForm.MainBox.RoleBox.RoleIdField;
import org.eclipse.scout.boot.tasks.commons.ui.admin.user.CreateUserPermission;
import org.eclipse.scout.boot.tasks.commons.ui.admin.user.UpdateUserPermission;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.IPermissionService;

@Bean
public class RoleForm extends AbstractForm {

  @Inject
  private RoleService roleService;

  public String getRoleId() {
    return getRoleIdField().getValue();
  }

  public void setRoleId(String roleId) {
    getRoleIdField().setValue(roleId);
  }

  @Override
  public Object computeExclusiveKey() {
    return getRoleId();
  }

  protected String calculateSubTitle() {
    return getRoleId();
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return IForm.DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Role");
  }

  public void startModify() {
    startInternalExclusive(new ModifyHandler());
  }

  public void startNew() {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public RoleIdField getRoleIdField() {
    return getFieldByClass(RoleIdField.class);
  }

  public PermissionTableField getPermissionTableField() {
    return getFieldByClass(PermissionTableField.class);
  }

  public RoleBox getRoleBox() {
    return getFieldByClass(RoleBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(1000)
  public class MainBox extends AbstractGroupBox {

    @Order(1000)
    public class RoleBox extends AbstractGroupBox {

      @Order(1000)
      public class RoleIdField extends AbstractStringField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("RoleName");
        }

        @Override
        protected boolean getConfiguredMandatory() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return 128;
        }
      }

      @Order(2000)
      public class PermissionTableField extends AbstractTableField<PermissionTableField.Table> {

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected int getConfiguredGridW() {
          return 2;
        }

        @Override
        protected int getConfiguredGridH() {
          return 4;
        }

        public class Table extends AbstractPermissionTable {

          @Override
          protected void execReloadPage() {
            reloadTableData();
          }
        }
      }
    }

    @Order(100000)
    public class OkButton extends AbstractOkButton {
    }

    @Order(101000)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      setEnabledPermission(new UpdateUserPermission());
      getRoleIdField().setEnabled(false);

      Role role = roleService.get(getRoleId());
      importFormFieldData(role);

      if (role.equals(Role.ROOT)) {
        getPermissionTableField().setEnabled(false);
      }

      setSubTitle(calculateSubTitle());
    }

    @Override
    protected void execStore() {
      Role role = roleService.get(getRoleIdField().getValue());
      exportFormFieldData(role);

      roleService.save(role);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }

    @Override
    protected boolean getConfiguredOpenExclusive() {
      return true;
    }
  }

  public class NewHandler extends AbstractDirtyFormHandler {

    @Override
    protected void execLoad() {
      setEnabledPermission(new CreateUserPermission());
      importFormFieldData(null);
    }

    @Override
    protected void execStore() {
      if (roleService.exists(getRoleId())) {
        throw new VetoException(TEXTS.get("AccountAlreadyExists", getRoleId()));
      }

      Role role = new Role();
      exportFormFieldData(role);

      roleService.save(role);
    }

    @Override
    protected void execDirtyStatusChanged(boolean dirty) {
      getForm().setSubTitle(calculateSubTitle());
    }
  }

  private void importFormFieldData(Role role) {
    if (role != null) {
      getRoleIdField().setValue(role.getId());
    }

    PermissionTableField.Table table = getPermissionTableField().getTable();
    table.loadData(null);
    table.getRows()
        .stream()
        .forEach(row -> {
          if (role != null) {
            String pId = table.getIdColumn().getValue(row);
            boolean assign = role.equals(Role.ROOT) || role.getPermissions().contains(pId);
            table.getAssignedColumn().setValue(row, assign);
          }
        });
  }

  protected Collection<Class<? extends Permission>> getPermissions() {
    return BEANS.get(IPermissionService.class).getAllPermissionClasses();
  }

  private void exportFormFieldData(Role role) {
    role.setId(getRoleIdField().getValue());

    RoleBox.PermissionTableField.Table table = getPermissionTableField().getTable();
    for (ITableRow row : table.getRows()) {
      String permission = table.getIdColumn().getValue(row);
      boolean assigned = table.getAssignedColumn().getValue(row);

      if (assigned) {
        role.getPermissions().add(permission);
      }
      else {
        role.getPermissions().remove(permission);
      }
    }
  }
}
