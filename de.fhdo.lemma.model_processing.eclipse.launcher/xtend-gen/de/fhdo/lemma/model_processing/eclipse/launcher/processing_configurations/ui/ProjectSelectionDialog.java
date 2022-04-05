package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui;

import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.xtext.xbase.lib.Conversions;

/**
 * Dialog to select files from an Eclipse project.
 * 
 * @author <a href="mailto:florian.rademacher@fh-dortmund.de">Florian Rademacher</a>
 */
@SuppressWarnings("all")
public class ProjectSelectionDialog extends ResourceListSelectionDialog {
  private boolean initializePattern = true;
  
  public ProjectSelectionDialog(final Shell parentShell) {
    super(parentShell, ResourcesPlugin.getWorkspace().getRoot(), IResource.PROJECT);
  }
  
  @Override
  public Control createDialogArea(final Composite parent) {
    final Control dialogArea = super.createDialogArea(parent);
    this.refresh(false);
    return dialogArea;
  }
  
  @Override
  public String adjustPattern() {
    String _xifexpression = null;
    if (this.initializePattern) {
      String _xblockexpression = null;
      {
        this.initializePattern = false;
        _xblockexpression = "*";
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = super.adjustPattern();
    }
    return _xifexpression;
  }
  
  public IProject getSelectedProject() {
    IProject _xifexpression = null;
    if (((this.getResult() != null) && (!((List<Object>)Conversions.doWrapArray(this.getResult())).isEmpty()))) {
      Object _get = this.getResult()[0];
      _xifexpression = ((IProject) _get);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
}
