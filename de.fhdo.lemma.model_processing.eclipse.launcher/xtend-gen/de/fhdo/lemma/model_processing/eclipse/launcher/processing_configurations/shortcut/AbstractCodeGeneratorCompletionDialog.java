package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut;

import com.google.common.base.Objects;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableTypesInformationManager;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractLaunchConfigurationTemplate;
import java.beans.PropertyChangeEvent;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public abstract class AbstractCodeGeneratorCompletionDialog extends AbstractLaunchConfigurationTemplate.AbstractTemplateCompletionDialog {
  protected static final int DEFAULT_WIDTH = 800;
  
  private static final Point DEFAULT_SIZE = new Point(AbstractCodeGeneratorCompletionDialog.DEFAULT_WIDTH, 300);
  
  private static final ILaunchManager LAUNCH_MANAGER = DebugPlugin.getDefault().getLaunchManager();
  
  private ProcessorExecutableType processorExecutableType;
  
  private Argument targetFolderArgument;
  
  protected Text processorExecutablePath;
  
  protected Text processorBasicExecutionCommand;
  
  protected Text targetFolder;
  
  protected final String generatorLongName;
  
  protected final String generatorShortName;
  
  protected AbstractCodeGeneratorCompletionDialog(final Shell parentShell, final IProject project, final IFile file, final String generatorLongName, final String generatorShortName) {
    super(parentShell, project, file);
    this.generatorLongName = generatorLongName;
    this.generatorShortName = generatorShortName;
  }
  
  protected final ProcessorExecutableType setProcessorExecutableType(final ProcessorExecutableType processorExecutableType) {
    return this.processorExecutableType = processorExecutableType;
  }
  
  protected final void setTargetFolderArgument(final Argument targetFolderArgument) {
    this.targetFolderArgument = targetFolderArgument;
    this.targetFolderArgument.addPropertyChangeListener(this);
  }
  
  @Override
  public void create() {
    super.create();
    this.configurationTemplate.setLaunchConfigurationName(AbstractCodeGeneratorCompletionDialog.LAUNCH_MANAGER.generateLaunchConfigurationName(this.getLaunchConfigurationNamePrefix(this.file)));
  }
  
  public final String getLaunchConfigurationNamePrefix(final IFile file) {
    final String fileBasename = FilenameUtils.getBaseName(file.getName());
    String _elvis = null;
    String _fileExtension = file.getFileExtension();
    if (_fileExtension != null) {
      _elvis = _fileExtension;
    } else {
      _elvis = "";
    }
    final String fileExtension = _elvis;
    String addendum = this.getLaunchConfigurationNameAddendum();
    boolean _isEmpty = addendum.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      String _replace = StringUtils.replace(addendum, " ", "_");
      String _plus = (_replace + "_");
      addendum = _plus;
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Run_");
    _builder.append(this.generatorShortName);
    _builder.append("_");
    _builder.append(addendum);
    _builder.append(fileBasename);
    _builder.append("_");
    _builder.append(fileExtension);
    return _builder.toString();
  }
  
  public String getLaunchConfigurationNameAddendum() {
    return "";
  }
  
  @Override
  public Control createDialogArea(final Composite parent) {
    Control _createDialogArea = super.createDialogArea(parent);
    final Composite area = ((Composite) _createDialogArea);
    final Composite container = new Composite(area, SWT.NONE);
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    container.setLayoutData(_gridData);
    GridLayout _gridLayout = new GridLayout(3, false);
    container.setLayout(_gridLayout);
    this.addConfigurationNameTextField(container, 2);
    this.addPathToProcessorExecutable(container);
    this.addProcessorBasicExecutionCommand(container);
    this.addTargetFolder(container);
    this.addAdditionalControlsToDialog(container);
    Shell _shell = parent.getShell();
    _shell.setSize(AbstractCodeGeneratorCompletionDialog.DEFAULT_SIZE);
    return area;
  }
  
  public void addAdditionalControlsToDialog(final Composite parent) {
  }
  
  private void addPathToProcessorExecutable(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    StringConcatenation _builder = new StringConcatenation();
    String _processorExecutableLabelText = this.getProcessorExecutableLabelText();
    _builder.append(_processorExecutableLabelText);
    _builder.append(":");
    _label.setText(_builder.toString());
    Text _text = new Text(parent, SWT.BORDER);
    this.processorExecutablePath = _text;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.processorExecutablePath.setLayoutData(_gridData);
    final Button processorExecutablePathSelectionButton = new Button(parent, SWT.PUSH);
    processorExecutablePathSelectionButton.setText("Browse...");
    final Listener _function = (Event it) -> {
      String _xtrycatchfinallyexpression = null;
      try {
        _xtrycatchfinallyexpression = ProcessorExecutableTypesInformationManager.inputSupportFunction(this.processorExecutableType).apply(this.getShell(), this.configurationTemplate);
      } catch (final Throwable _t) {
        if (_t instanceof IllegalArgumentException) {
          final IllegalArgumentException ex = (IllegalArgumentException)_t;
          Object _xblockexpression = null;
          {
            MessageDialog.openError(this.getShell(), "Input Support Error", ex.getMessage());
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((String)_xblockexpression);
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final String selectedExecutablePath = _xtrycatchfinallyexpression;
      if ((selectedExecutablePath != null)) {
        this.processorExecutablePath.setText(selectedExecutablePath);
      }
    };
    processorExecutablePathSelectionButton.addListener(SWT.Selection, _function);
    final Procedure1<String> _function_1 = (String it) -> {
      ProcessingConfigurationWithLaunchConfigurationName.validProcessorExecutablePath(
        this.configurationTemplate.getProcessorExecutableType(), 
        this.configurationTemplate.getProcessorBasicExecutionCommand(), it);
    };
    this.<ProcessingConfigurationWithLaunchConfigurationName>bindWithValidationDecorationSupport(
      this.processorExecutablePath, 
      ProcessingConfigurationWithLaunchConfigurationName.class, 
      "processorExecutablePath", 
      this.configurationTemplate, _function_1);
  }
  
  private String getProcessorExecutableLabelText() {
    String labelAddendum = this.getProcessorExecutableLabelTextAddendum();
    boolean _isEmpty = labelAddendum.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      labelAddendum = (" " + labelAddendum);
    }
    return (this.generatorLongName + labelAddendum);
  }
  
  public String getProcessorExecutableLabelTextAddendum() {
    return "";
  }
  
  private void addProcessorBasicExecutionCommand(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Basic processor execution command:");
    Text _text = new Text(parent, SWT.BORDER);
    this.processorBasicExecutionCommand = _text;
    final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
    layoutData.horizontalSpan = 2;
    this.processorBasicExecutionCommand.setLayoutData(layoutData);
    final Procedure1<String> _function = (String it) -> {
      ProcessingConfigurationWithLaunchConfigurationName.validProcessorBasicExecutionCommand(
        this.configurationTemplate.getProcessorExecutableType(), it);
    };
    this.<ProcessingConfigurationWithLaunchConfigurationName>bindWithValidationDecorationSupport(
      this.processorBasicExecutionCommand, 
      ProcessingConfigurationWithLaunchConfigurationName.class, 
      "processorBasicExecutionCommand", 
      this.configurationTemplate, _function);
  }
  
  private void addTargetFolder(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Generation target folder:");
    Text _text = new Text(parent, SWT.BORDER);
    this.targetFolder = _text;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.targetFolder.setLayoutData(_gridData);
    final Button folderSelectionButton = new Button(parent, SWT.PUSH);
    folderSelectionButton.setText("Browse...");
    final Listener _function = (Event it) -> {
      Shell _shell = this.getShell();
      final DirectoryDialog directoryDialog = new DirectoryDialog(_shell, SWT.OPEN);
      directoryDialog.setText("Select target folder for generated artifacts");
      directoryDialog.setFilterPath(this.configurationTemplate.getSourceModelFile().getProject().getLocation().makeAbsolute().toString());
      final String selectedFolder = directoryDialog.open();
      if ((selectedFolder != null)) {
        this.targetFolder.setText(selectedFolder);
      }
    };
    folderSelectionButton.addListener(SWT.Selection, _function);
    final Procedure1<String> _function_1 = (String it) -> {
      this.targetFolderArgument.getType().checkValidValueInUserRepresentation(this.configurationTemplate, it);
    };
    this.<Argument>bindWithValidationDecorationSupport(
      this.targetFolder, 
      Argument.class, 
      "value", 
      this.targetFolderArgument, _function_1);
  }
  
  @Override
  public void reactToPropertyChange(final PropertyChangeEvent event) {
    String _propertyName = event.getPropertyName();
    boolean _equals = Objects.equal(_propertyName, "processorBasicExecutionCommand");
    if (_equals) {
      Utils.triggerValidation(this.processorExecutablePath);
    }
  }
  
  @Override
  public boolean close() {
    boolean _xblockexpression = false;
    {
      this.targetFolderArgument.removePropertyChangeListener(this);
      _xblockexpression = super.close();
    }
    return _xblockexpression;
  }
}
