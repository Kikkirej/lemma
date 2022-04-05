package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations;

import com.google.common.base.Objects;
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ConstantParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.MultiValuedParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractIntermediateModelArgumentTypeWithEnumValue;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeIdentifier;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.ProjectFileSelectionDialog;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.ProjectSelectionDialog;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table.ArgumentsTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LaunchConfigurationTab extends AbstractLaunchConfigurationTab implements PropertyChangeListener {
  private static final ProcessorExecutableType DEFAULT_PROCESSOR_EXECUTABLE_TYPE = ProcessorExecutableType.LOCAL_JAVA_PROGRAM;
  
  /**
   * static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE = ArgumentTypeFactory
   * .fromIdentifier(SourceModelArgumentType.IDENTIFIER) as SourceModelArgumentType
   * static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_VALUE = DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE
   * .getValidLiteralStringValues().get(SourceModelKind.SELECTED_FILE)
   * static val DEFAULT_SOURCE_MODEL_FILE_ARGUMENT = new Argument(
   * ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER),
   * DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_TYPE,
   * "-s",
   * DEFAULT_SOURCE_MODEL_FILE_ARGUMENT_VALUE
   * )
   */
  private static final Argument DEFAULT_SOURCE_MODEL_FILE_ARGUMENT = Argument.newArgument().singleValued().sourceModel().parameter("-s");
  
  private static final Set<AbstractConstraintType> SUPPORTED_CONSTRAINT_TYPES = Collections.<AbstractConstraintType>unmodifiableSet(CollectionLiterals.<AbstractConstraintType>newHashSet(ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.FILENAME_REGEX), ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.MODEL_KIND)));
  
  private static final Set<AbstractArgumentKind> SUPPORTED_ARGUMENT_KINDS = Collections.<AbstractArgumentKind>unmodifiableSet(CollectionLiterals.<AbstractArgumentKind>newHashSet(ArgumentKindFactory.fromIdentifier(ConstantParameterArgumentKind.IDENTIFIER), ArgumentKindFactory.fromIdentifier(MultiValuedParameterArgumentKind.IDENTIFIER), ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER)));
  
  private final ArrayList<Supplier<ControlDecoration>> CONFIGURATION_BINDERS = CollectionLiterals.<Supplier<ControlDecoration>>newArrayList(
    ((Supplier<ControlDecoration>) () -> {
      return this.bindSourceModelProjectName();
    }), 
    ((Supplier<ControlDecoration>) () -> {
      return this.bindSourceModelFilePath();
    }), 
    ((Supplier<ControlDecoration>) () -> {
      return this.bindProcessorExecutableType();
    }), 
    ((Supplier<ControlDecoration>) () -> {
      return this.bindProcessorBasicExecutionCommand();
    }), 
    ((Supplier<ControlDecoration>) () -> {
      return this.bindProcessorExecutablePath();
    }));
  
  private final DataBindingContext dataBindingContext = new DataBindingContext();
  
  private final ArrayList<ControlDecoration> currentDecorations = CollectionLiterals.<ControlDecoration>newArrayList();
  
  private Composite mainComposite;
  
  private Text sourceModelProjectName;
  
  private Text sourceModelFilePath;
  
  private Combo processorExecutableType;
  
  private Text processorBasicExecutionCommand;
  
  private Text processorExecutablePath;
  
  private ControlDecoration processorExecutablePathDecorator;
  
  private ArgumentsTable argumentsTable;
  
  private boolean initializationDone = false;
  
  private boolean sourceModelArgumentWasAddedOnce = false;
  
  private ProcessingConfiguration originalConfiguration;
  
  private ProcessingConfiguration currentConfiguration;
  
  private ProcessorExecutableType previousProcessorExecutableType;
  
  @Override
  public void createControl(final Composite parent) {
    Composite _composite = new Composite(parent, SWT.NULL);
    this.mainComposite = _composite;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    this.mainComposite.setLayoutData(_gridData);
    GridLayout _gridLayout = new GridLayout(1, false);
    this.mainComposite.setLayout(_gridLayout);
    this.setControl(this.mainComposite);
    this.addResourceInputs(this.mainComposite);
    this.addProcessorInputs(this.mainComposite);
    this.addArgumentsTable(this.mainComposite);
  }
  
  private void addResourceInputs(final Composite parent) {
    final Composite resourceInputsGrid = new Composite(parent, SWT.NULL);
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    resourceInputsGrid.setLayoutData(_gridData);
    GridLayout _gridLayout = new GridLayout(3, false);
    resourceInputsGrid.setLayout(_gridLayout);
    this.addSourceModelProject(resourceInputsGrid);
    this.addSourceModelFile(resourceInputsGrid);
  }
  
  private void addSourceModelProject(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Source model project:");
    Text _text = new Text(parent, SWT.BORDER);
    this.sourceModelProjectName = _text;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.sourceModelProjectName.setLayoutData(_gridData);
    final Button projectSelectionButton = new Button(parent, SWT.PUSH);
    projectSelectionButton.setText("Browse...");
    final Listener _function = (Event it) -> {
      this.handleSourceModelProjectSelection(parent.getShell());
    };
    projectSelectionButton.addListener(SWT.Selection, _function);
  }
  
  private void handleSourceModelProjectSelection(final Shell shell) {
    IProject selectedProject = null;
    final ProjectSelectionDialog dialog = new ProjectSelectionDialog(shell);
    int _open = dialog.open();
    boolean _notEquals = (_open != Window.CANCEL);
    if (_notEquals) {
      selectedProject = dialog.getSelectedProject();
      this.sourceModelProjectName.setText(selectedProject.getName());
    }
    try {
      String _name = null;
      if (selectedProject!=null) {
        _name=selectedProject.getName();
      }
      ProcessingConfiguration.validSourceModelFilePath(_name, 
        this.sourceModelFilePath.getText());
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        this.sourceModelFilePath.setText("");
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  private void addSourceModelFile(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Source model file:");
    Text _text = new Text(parent, SWT.BORDER);
    this.sourceModelFilePath = _text;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.sourceModelFilePath.setLayoutData(_gridData);
    final Button fileSelectionButton = new Button(parent, SWT.PUSH);
    fileSelectionButton.setText("Browse...");
    final Listener _function = (Event it) -> {
      this.handleSourceModelFileSelection(parent.getShell());
    };
    fileSelectionButton.addListener(SWT.Selection, _function);
  }
  
  private void handleSourceModelFileSelection(final Shell shell) {
    final IProject selectedProject = Utils.findProjectInCurrentWorkspace(this.sourceModelProjectName.getText());
    IFile selectedFile = null;
    if ((selectedProject != null)) {
      final ProjectFileSelectionDialog dialog = new ProjectFileSelectionDialog(shell, selectedProject);
      int _open = dialog.open();
      boolean _notEquals = (_open != Window.CANCEL);
      if (_notEquals) {
        selectedFile = dialog.getSelectedFile();
      }
    } else {
      IWorkspaceRoot _root = ResourcesPlugin.getWorkspace().getRoot();
      final ResourceListSelectionDialog dialog_1 = new ResourceListSelectionDialog(shell, _root, 
        IResource.FILE);
      if (((dialog_1.open() != Window.CANCEL) && (!((List<Object>)Conversions.doWrapArray(dialog_1.getResult())).isEmpty()))) {
        Object _get = dialog_1.getResult()[0];
        selectedFile = ((IFile) _get);
      }
    }
    if ((selectedFile != null)) {
      if ((selectedProject == null)) {
        this.sourceModelProjectName.setText(selectedFile.getProject().getName());
      }
      this.sourceModelFilePath.setText(selectedFile.getProjectRelativePath().toString());
    }
  }
  
  private void addProcessorInputs(final Composite parent) {
    final Group inputGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    inputGroup.setText("Processor Information");
    GridLayout _gridLayout = new GridLayout(2, false);
    inputGroup.setLayout(_gridLayout);
    GridData _gridData = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
    inputGroup.setLayoutData(_gridData);
    this.addTypeOfProcessorExecutable(inputGroup);
    this.addPathToProcessorExecutable(inputGroup);
    this.addBasicProcessorExecutionCommand(inputGroup);
  }
  
  private void addTypeOfProcessorExecutable(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Type of processor executable:");
    Combo _combo = new Combo(parent, (SWT.DROP_DOWN | SWT.READ_ONLY));
    this.processorExecutableType = _combo;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.processorExecutableType.setLayoutData(_gridData);
    final Function1<ProcessorExecutableType, String> _function = (ProcessorExecutableType it) -> {
      return ProcessorExecutableTypesInformationManager.label(it);
    };
    final Consumer<String> _function_1 = (String it) -> {
      this.processorExecutableType.add(it);
    };
    ListExtensions.<ProcessorExecutableType, String>map(((List<ProcessorExecutableType>)Conversions.doWrapArray(ProcessorExecutableType.values())), _function).forEach(_function_1);
    this.processorExecutableType.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetDefaultSelected(final SelectionEvent e) {
        this.widgetSelected(e);
      }
      
      @Override
      public void widgetSelected(final SelectionEvent e) {
        LaunchConfigurationTab.this.toggleProcessorExecutablePathDecorator();
        boolean _resetBasicProcessorExecutionCommandOnExecutableTypeSwitch = LaunchConfigurationTab.this.resetBasicProcessorExecutionCommandOnExecutableTypeSwitch();
        boolean _not = (!_resetBasicProcessorExecutionCommandOnExecutableTypeSwitch);
        if (_not) {
          Utils.triggerValidation(LaunchConfigurationTab.this.processorBasicExecutionCommand);
        }
      }
    });
  }
  
  private void addPathToProcessorExecutable(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Path to processor executable:");
    Text _text = new Text(parent, SWT.BORDER);
    this.processorExecutablePath = _text;
    final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
    layoutData.widthHint = 50;
    this.processorExecutablePath.setLayoutData(layoutData);
    ControlDecoration _controlDecoration = new ControlDecoration(this.processorExecutablePath, 
      (SWT.TOP | SWT.RIGHT));
    this.processorExecutablePathDecorator = _controlDecoration;
    this.processorExecutablePathDecorator.setDescriptionText(("Click to receive input support for " + 
      "the selected type of the processor executable"));
    this.processorExecutablePathDecorator.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
    this.processorExecutablePathDecorator.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetDefaultSelected(final SelectionEvent e) {
        this.widgetDefaultSelected(e);
      }
      
      @Override
      public void widgetSelected(final SelectionEvent e) {
        String _xtrycatchfinallyexpression = null;
        try {
          Function2<? super Shell, ? super ProcessingConfiguration, ? extends String> _inputSupportFunction = ProcessorExecutableTypesInformationManager.inputSupportFunction(LaunchConfigurationTab.this.currentConfiguration.getProcessorExecutableType());
          String _apply = null;
          if (_inputSupportFunction!=null) {
            _apply=_inputSupportFunction.apply(LaunchConfigurationTab.this.getShell(), LaunchConfigurationTab.this.currentConfiguration);
          }
          _xtrycatchfinallyexpression = _apply;
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            final IllegalArgumentException ex = (IllegalArgumentException)_t;
            Object _xblockexpression = null;
            {
              MessageDialog.openError(LaunchConfigurationTab.this.getShell(), "Input Support Error", ex.getMessage());
              _xblockexpression = null;
            }
            _xtrycatchfinallyexpression = ((String)_xblockexpression);
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        final String selectedInput = _xtrycatchfinallyexpression;
        if ((selectedInput != null)) {
          LaunchConfigurationTab.this.processorExecutablePath.setText(selectedInput);
        }
      }
    });
  }
  
  private void addBasicProcessorExecutionCommand(final Composite parent) {
    Label _label = new Label(parent, SWT.NULL);
    _label.setText("Basic processor execution command:");
    Text _text = new Text(parent, SWT.BORDER);
    this.processorBasicExecutionCommand = _text;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
    this.processorBasicExecutionCommand.setLayoutData(_gridData);
    final ControlDecoration executionCommandHint = new ControlDecoration(this.processorBasicExecutionCommand, 
      (SWT.TOP | SWT.RIGHT));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("processor executable, e.g., \"java\" or \"docker\"");
    String _plus = ("The basic execution command to invoke the " + _builder);
    executionCommandHint.setDescriptionText(_plus);
    executionCommandHint.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
  }
  
  @Override
  public void initializeFrom(final ILaunchConfiguration launchConfiguration) {
    ProcessingConfiguration _elvis = null;
    ProcessingConfiguration _deserializeFrom = ProcessingConfiguration.deserializeFrom(launchConfiguration);
    if (_deserializeFrom != null) {
      _elvis = _deserializeFrom;
    } else {
      ProcessingConfiguration _prepareNewProcessingConfigurationInstance = this.prepareNewProcessingConfigurationInstance();
      _elvis = _prepareNewProcessingConfigurationInstance;
    }
    final ProcessingConfiguration configuration = _elvis;
    this.setCurrentConfiguration(configuration);
    this.toggleProcessorExecutablePathDecorator();
    this.initializationDone = true;
  }
  
  private ProcessingConfiguration prepareNewProcessingConfigurationInstance() {
    String _get = LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(LaunchConfigurationTab.DEFAULT_PROCESSOR_EXECUTABLE_TYPE);
    return new ProcessingConfiguration(
      "", 
      "", 
      LaunchConfigurationTab.DEFAULT_PROCESSOR_EXECUTABLE_TYPE, 
      "", _get);
  }
  
  private void setCurrentConfiguration(final ProcessingConfiguration configuration) {
    this.removeCurrentConfigurationBindings();
    if (this.currentConfiguration!=null) {
      this.currentConfiguration.removePropertyChangeListener(this);
    }
    this.originalConfiguration = configuration;
    Object _clone = configuration.clone();
    this.currentConfiguration = ((ProcessingConfiguration) _clone);
    this.originalConfiguration.convertToUserRepresentation();
    this.currentConfiguration.convertToUserRepresentation();
    this.argumentsTable.removePropertyChangeListener(this);
    this.argumentsTable.setInput(this.currentConfiguration);
    this.argumentsTable.addPropertyChangeListener(this);
    this.establishCurrentConfigurationBindings();
    this.currentConfiguration.addPropertyChangeListener(this);
  }
  
  private void removeCurrentConfigurationBindings() {
    final Consumer<Binding> _function = (Binding it) -> {
      it.dispose();
      this.dataBindingContext.removeBinding(it);
    };
    this.dataBindingContext.getBindings().forEach(_function);
    final Consumer<ControlDecoration> _function_1 = (ControlDecoration it) -> {
      it.dispose();
    };
    this.currentDecorations.forEach(_function_1);
    this.currentDecorations.clear();
  }
  
  private void establishCurrentConfigurationBindings() {
    final Consumer<Supplier<ControlDecoration>> _function = (Supplier<ControlDecoration> binder) -> {
      final ControlDecoration decoration = binder.get();
      if ((decoration != null)) {
        this.currentDecorations.add(decoration);
      }
    };
    this.CONFIGURATION_BINDERS.forEach(_function);
  }
  
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    String _propertyName = event.getPropertyName();
    if (_propertyName != null) {
      switch (_propertyName) {
        case "sourceModelProjectName":
        case "sourceModelFilePath":
          this.addSourceModelFileArgumentIfAllowed();
          this.argumentsTable.updateArguments(AbstractIntermediateModelArgumentTypeWithEnumValue.class);
          break;
        case "processorBasicExecutionCommand":
          Utils.triggerValidation(this.processorExecutablePath);
          break;
      }
    }
    this.updateLaunchConfigurationDialog();
  }
  
  private void addSourceModelFileArgumentIfAllowed() {
    if ((((!this.initializationDone) || 
      this.sourceModelArgumentWasAddedOnce) || 
      this.argumentsTable.containsArgument(LaunchConfigurationTab.DEFAULT_SOURCE_MODEL_FILE_ARGUMENT))) {
      return;
    }
    try {
      ProcessingConfiguration.validSourceModelFilePath(
        this.currentConfiguration.getSourceModelProjectName(), 
        this.currentConfiguration.getSourceModelFilePath());
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        return;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    boolean _supportsTranformation = ProgrammaticIntermediateModelTransformation.supportsTranformation(this.currentConfiguration.getSourceModelFile());
    if (_supportsTranformation) {
      this.argumentsTable.addArgument(LaunchConfigurationTab.DEFAULT_SOURCE_MODEL_FILE_ARGUMENT, 0);
      this.sourceModelArgumentWasAddedOnce = true;
    }
  }
  
  /**
   * private def bindConfigurationName() {
   * val target = WidgetProperties.text(SWT.Modify).observe(configurationName)
   * val model = BeanProperties.value(ProcessingConfiguration, "name")
   * .observe(currentConfiguration)
   * //val updateStrategy = new UpdateValueStrategy<String, String>()
   * /*updateStrategy.afterGetValidator = [value |
   * return try {
   * ProcessingConfiguration.validName(value)
   * ValidationStatus.ok()
   * } catch (IllegalArgumentException ex)
   * ValidationStatus.error(ex.message)
   * 
   * val existingConfiguration = configurations.get(value)
   * val duplicateConfigurationName = existingConfiguration !== null &&
   * existingConfiguration !== originalConfiguration
   * return if (duplicateConfigurationName)
   * ValidationStatus.error("Another configuration with this name already exists")
   * else
   * ValidationStatus.ok()
   * ]
   * //val bindValue = DATA_BINDING_CONTEXT.bindValue(target, model, updateStrategy, null)
   * val bindValue = DATA_BINDING_CONTEXT.bindValue(target, model, null, null)
   * val decoration = ControlDecorationSupport.create(bindValue, SWT.TOP.bitwiseOr(SWT.LEFT))
   * return bindValue -> decoration
   * }
   */
  private ControlDecoration bindSourceModelProjectName() {
    final Procedure1<String> _function = (String it) -> {
      ProcessingConfiguration.validSourceModelProjectName(it);
    };
    return this.<ProcessingConfiguration>bindWithValidationDecorationSupport(this.sourceModelProjectName, 
      ProcessingConfiguration.class, 
      "sourceModelProjectName", 
      this.currentConfiguration, _function);
  }
  
  private <T extends Object> ControlDecoration bindWithValidationDecorationSupport(final Text field, final Class<T> beanClass, final String propertyName, final T source, final Procedure1<? super String> validationProcedure) {
    return Utils.<T>bindWithValidationDecorationSupport(field, 
      this.dataBindingContext, beanClass, propertyName, source, validationProcedure).getValue();
  }
  
  private ControlDecoration bindSourceModelFilePath() {
    final Procedure1<String> _function = (String it) -> {
      ProcessingConfiguration.validSourceModelFilePath(
        this.currentConfiguration.getSourceModelProjectName(), it);
    };
    return this.<ProcessingConfiguration>bindWithValidationDecorationSupport(this.sourceModelFilePath, 
      ProcessingConfiguration.class, 
      "sourceModelFilePath", 
      this.currentConfiguration, _function);
  }
  
  private ControlDecoration bindProcessorExecutableType() {
    final ISWTObservableValue<String> target = WidgetProperties.comboSelection().observe(this.processorExecutableType);
    final IObservableValue<ProcessorExecutableType> model = BeanProperties.<ProcessingConfiguration, ProcessorExecutableType>value(ProcessingConfiguration.class, "processorExecutableType", 
      ProcessorExecutableType.class).observe(this.currentConfiguration);
    final UpdateValueStrategy<ProcessorExecutableType, String> modelToTargetConverter = new UpdateValueStrategy<ProcessorExecutableType, String>();
    final Function<ProcessorExecutableType, String> _function = (ProcessorExecutableType execType) -> {
      return ProcessorExecutableTypesInformationManager.label(execType);
    };
    modelToTargetConverter.setConverter(IConverter.<ProcessorExecutableType, String>create(_function));
    final UpdateValueStrategy<String, ProcessorExecutableType> targetToModelConverter = new UpdateValueStrategy<String, ProcessorExecutableType>();
    final Function<String, ProcessorExecutableType> _function_1 = (String label) -> {
      ProcessorExecutableType _xblockexpression = null;
      {
        this.previousProcessorExecutableType = this.currentConfiguration.getProcessorExecutableType();
        _xblockexpression = ProcessorExecutableTypesInformationManager.literal(label);
      }
      return _xblockexpression;
    };
    targetToModelConverter.setConverter(IConverter.<String, ProcessorExecutableType>create(_function_1));
    this.dataBindingContext.<String, ProcessorExecutableType>bindValue(target, model, targetToModelConverter, modelToTargetConverter);
    return null;
  }
  
  private ControlDecoration bindProcessorBasicExecutionCommand() {
    final Procedure1<String> _function = (String it) -> {
      ProcessingConfiguration.validProcessorBasicExecutionCommand(
        this.currentConfiguration.getProcessorExecutableType(), it);
    };
    return this.<ProcessingConfiguration>bindWithValidationDecorationSupport(this.processorBasicExecutionCommand, 
      ProcessingConfiguration.class, 
      "processorBasicExecutionCommand", 
      this.currentConfiguration, _function);
  }
  
  private ControlDecoration bindProcessorExecutablePath() {
    final Procedure1<String> _function = (String it) -> {
      ProcessingConfiguration.validProcessorExecutablePath(
        this.currentConfiguration.getProcessorExecutableType(), 
        this.currentConfiguration.getProcessorBasicExecutionCommand(), it);
    };
    return this.<ProcessingConfiguration>bindWithValidationDecorationSupport(this.processorExecutablePath, 
      ProcessingConfiguration.class, 
      "processorExecutablePath", 
      this.currentConfiguration, _function);
  }
  
  private Object prepareInputFields() {
    return null;
  }
  
  private void toggleProcessorExecutablePathDecorator() {
    final boolean executableTypeHasInputSupport = ProcessorExecutableTypesInformationManager.hasInputSupport(this.currentConfiguration.getProcessorExecutableType());
    if (executableTypeHasInputSupport) {
      this.processorExecutablePathDecorator.show();
    } else {
      this.processorExecutablePathDecorator.hide();
    }
  }
  
  private boolean resetBasicProcessorExecutionCommandOnExecutableTypeSwitch() {
    final String previousTypeDefaultCommand = LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(this.previousProcessorExecutableType);
    final String newTypeDefaultCommand = LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(this.currentConfiguration.getProcessorExecutableType());
    if (((previousTypeDefaultCommand == null) || (newTypeDefaultCommand == null))) {
      return false;
    }
    boolean _xifexpression = false;
    String _processorBasicExecutionCommand = this.currentConfiguration.getProcessorBasicExecutionCommand();
    boolean _equals = Objects.equal(previousTypeDefaultCommand, _processorBasicExecutionCommand);
    if (_equals) {
      _xifexpression = this.setDefaultBasicProcessorExecutionCommand(this.currentConfiguration);
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  private boolean setDefaultBasicProcessorExecutionCommand(final ProcessingConfiguration configuration) {
    final String defaultCommand = LaunchConfigurationConstants.SUPPORTED_DEFAULT_BASIC_PROCESSOR_EXECUTION_COMMANDS.get(configuration.getProcessorExecutableType());
    if ((defaultCommand == null)) {
      return false;
    }
    configuration.setProcessorBasicExecutionCommand(defaultCommand);
    return true;
  }
  
  private void addConstraintsTable(final Composite parent) {
    final Group tableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
    tableGroup.setText("Constraints");
    GridLayout _gridLayout = new GridLayout(1, false);
    tableGroup.setLayout(_gridLayout);
    final ControlDecoration constraintLinkageHint = new ControlDecoration(tableGroup, (SWT.TOP | SWT.RIGHT));
    constraintLinkageHint.setDescriptionText(("Target elements must adhere to at least one " + 
      "constraint of each type for the configuration to be invokable on them"));
    constraintLinkageHint.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
    final GridData tableGroupData = new GridData();
    tableGroupData.grabExcessHorizontalSpace = true;
    tableGroupData.grabExcessVerticalSpace = true;
    tableGroupData.horizontalAlignment = GridData.FILL;
    tableGroupData.verticalAlignment = GridData.FILL;
    tableGroup.setLayoutData(tableGroupData);
  }
  
  private ArgumentsTable addArgumentsTable(final Composite parent) {
    ArgumentsTable _xblockexpression = null;
    {
      final Group tableGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
      tableGroup.setText("Arguments");
      GridLayout _gridLayout = new GridLayout(1, false);
      tableGroup.setLayout(_gridLayout);
      GridData _gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
      tableGroup.setLayoutData(_gridData);
      ArgumentsTable _argumentsTable = new ArgumentsTable(tableGroup, LaunchConfigurationTab.SUPPORTED_ARGUMENT_KINDS);
      _xblockexpression = this.argumentsTable = _argumentsTable;
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean isValid(final ILaunchConfiguration configuration) {
    boolean _xtrycatchfinallyexpression = false;
    try {
      boolean _xblockexpression = false;
      {
        this.currentConfiguration.validateInUserRepresentation();
        _xblockexpression = true;
      }
      _xtrycatchfinallyexpression = _xblockexpression;
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        _xtrycatchfinallyexpression = false;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  /**
   * override canSave() {
   * return !erroneousInput()
   * }
   */
  @Override
  public boolean isDirty() {
    return (!Objects.equal(this.originalConfiguration, this.currentConfiguration));
  }
  
  private Object erroneousInput() {
    return null;
  }
  
  /**
   * private def erroneousConstraintInputs() {
   * currentConfiguration.constraints !== null &&
   * currentConfiguration.constraints.exists[
   * try {
   * type.checkValidValueInUserRepresentation(value)
   * false
   * } catch (IllegalArgumentException ex) {
   * true
   * }
   * ]
   * }
   */
  @Override
  public void performApply(final ILaunchConfigurationWorkingCopy launchConfiguration) {
    this.currentConfiguration.convertToInternalRepresentation();
    ProcessingConfiguration.setProcessingConfigurationAsAttribute(launchConfiguration, 
      this.currentConfiguration);
    this.currentConfiguration.convertToUserRepresentation();
  }
  
  @Override
  public String getName() {
    return LaunchConfigurationConstants.COMMON_LAUNCH_CONFIGURATION_TAB_NAME;
  }
  
  @Override
  public Image getImage() {
    return LaunchConfigurationConstants.COMMON_LAUNCH_CONFIGURATION_TAB_IMAGE;
  }
  
  @Override
  public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
  }
  
  @Override
  public void dispose() {
    this.argumentsTable.dispose();
    this.dataBindingContext.dispose();
    final Consumer<ControlDecoration> _function = (ControlDecoration it) -> {
      it.dispose();
    };
    this.currentDecorations.forEach(_function);
    super.dispose();
  }
}
