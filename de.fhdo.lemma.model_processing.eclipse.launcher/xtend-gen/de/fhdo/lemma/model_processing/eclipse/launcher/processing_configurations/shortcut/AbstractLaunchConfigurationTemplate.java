package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.CommandLineGenerator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public abstract class AbstractLaunchConfigurationTemplate {
  public static abstract class AbstractTemplateCompletionDialog extends TitleAreaDialog implements PropertyChangeListener {
    protected final DataBindingContext dataBindingContext = new DataBindingContext();
    
    protected final ArrayList<Binding> bindings = CollectionLiterals.<Binding>newArrayList();
    
    private final ArrayList<ControlDecoration> decorations = CollectionLiterals.<ControlDecoration>newArrayList();
    
    private Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> runCallback;
    
    private Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> storeCallback;
    
    protected final IProject project;
    
    protected final IFile file;
    
    private Button showCommandLineButton;
    
    private Button storeButton;
    
    private Button runButton;
    
    @Accessors(AccessorType.PROTECTED_GETTER)
    protected ProcessingConfigurationWithLaunchConfigurationName configurationTemplate;
    
    protected AbstractTemplateCompletionDialog(final Shell parentShell, final IProject project, final IFile file) {
      super(parentShell);
      this.project = project;
      this.file = file;
      this.setHelpAvailable(false);
    }
    
    public final void setConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName configurationTemplate) {
      this.configurationTemplate = configurationTemplate;
      this.configurationTemplate.addPropertyChangeListener(this);
    }
    
    @Override
    public final void propertyChange(final PropertyChangeEvent event) {
      Object _clone = this.configurationTemplate.clone();
      final ProcessingConfigurationWithLaunchConfigurationName completedConfiguration = ((ProcessingConfigurationWithLaunchConfigurationName) _clone);
      this.completeProcessingConfigurationTemplate(completedConfiguration);
      final boolean configurationIsValid = this.isValid(completedConfiguration);
      this.showCommandLineButton.setEnabled(configurationIsValid);
      this.storeButton.setEnabled(configurationIsValid);
      this.runButton.setEnabled(configurationIsValid);
      this.reactToPropertyChange(event);
    }
    
    public void reactToPropertyChange(final PropertyChangeEvent evt) {
    }
    
    private boolean isValid(final ProcessingConfigurationWithLaunchConfigurationName configuration) {
      boolean _xtrycatchfinallyexpression = false;
      try {
        boolean _xblockexpression = false;
        {
          configuration.validateInUserRepresentation();
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
    
    public final Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> setStoreCallback(final Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> callback) {
      return this.storeCallback = callback;
    }
    
    public final Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> setRunCallback(final Procedure1<? super ProcessingConfigurationWithLaunchConfigurationName> callback) {
      return this.runCallback = callback;
    }
    
    @Override
    public void createButtonsForButtonBar(final Composite parent) {
      this.createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
      this.showCommandLineButton = this.createButton(parent, IDialogConstants.DETAILS_ID, 
        "Show Command Line", false);
      this.storeButton = this.createButton(parent, IDialogConstants.FINISH_ID, "Store Without Running", 
        false);
      this.runButton = this.createButton(parent, IDialogConstants.OK_ID, "Run", true);
    }
    
    @Override
    public void buttonPressed(final int buttonId) {
      switch (buttonId) {
        case IDialogConstants.DETAILS_ID:
          this.showCommandLinePressed();
          break;
        case IDialogConstants.FINISH_ID:
          this.storePressed();
          break;
        default:
          super.buttonPressed(buttonId);
          break;
      }
    }
    
    private void showCommandLinePressed() {
      Object _clone = this.configurationTemplate.clone();
      final ProcessingConfigurationWithLaunchConfigurationName completedTemplate = ((ProcessingConfigurationWithLaunchConfigurationName) _clone);
      this.completeProcessingConfigurationTemplate(completedTemplate);
      final CommandLineGenerator commandLineGenerator = new CommandLineGenerator(completedTemplate);
      CommandLineGenerator.CoherentCommandLineParts _xtrycatchfinallyexpression = null;
      try {
        CommandLineGenerator.CoherentCommandLineParts _xblockexpression = null;
        {
          final Pair<CommandLineGenerator.CoherentCommandLineParts, List<String>> partsAndWarnings = commandLineGenerator.generateCoherentCommandLineParts();
          final Consumer<String> _function = (String it) -> {
            MessageDialog.openWarning(this.getShell(), "Warning during commandline generation", it);
          };
          partsAndWarnings.getValue().forEach(_function);
          _xblockexpression = partsAndWarnings.getKey();
        }
        _xtrycatchfinallyexpression = _xblockexpression;
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception ex = (Exception)_t;
          MessageDialog.openError(this.getShell(), "Error during commandline generation", 
            ex.getMessage());
          return;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      final CommandLineGenerator.CoherentCommandLineParts commandLineParts = _xtrycatchfinallyexpression;
      Shell _shell = this.getShell();
      String _printableCommandLine = commandLineGenerator.toPrintableCommandLine(commandLineParts, "\n\t");
      final ShowCommandLineDialog commandLineDialog = new ShowCommandLineDialog(_shell, _printableCommandLine);
      commandLineDialog.open();
    }
    
    private Object storePressed() {
      Object _xtrycatchfinallyexpression = null;
      try {
        this.completeProcessingConfigurationTemplate(this.configurationTemplate);
        this.storeCallback.apply(this.configurationTemplate);
        super.okPressed();
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          _xtrycatchfinallyexpression = null;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
      return _xtrycatchfinallyexpression;
    }
    
    @Override
    public void okPressed() {
      try {
        this.completeProcessingConfigurationTemplate(this.configurationTemplate);
        this.runCallback.apply(this.configurationTemplate);
        super.okPressed();
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
    
    public void completeProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName templateToComplete) {
    }
    
    protected final void addConfigurationNameTextField(final Composite parent, final int columnSpan) {
      Label _label = new Label(parent, SWT.NULL);
      _label.setText("Launch configuration name:");
      final Text nameTextField = new Text(parent, SWT.BORDER);
      final GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
      if ((columnSpan > 0)) {
        layoutData.horizontalSpan = columnSpan;
      }
      nameTextField.setLayoutData(layoutData);
      final Procedure1<String> _function = (String it) -> {
        ProcessingConfigurationWithLaunchConfigurationName.validLaunchConfigurationName(it);
      };
      this.<ProcessingConfigurationWithLaunchConfigurationName>bindWithValidationDecorationSupport(nameTextField, 
        ProcessingConfigurationWithLaunchConfigurationName.class, 
        "launchConfigurationName", 
        this.configurationTemplate, _function);
    }
    
    protected final <T extends Object> void bindWithValidationDecorationSupport(final Control control, final Class<T> beanClass, final String propertyName, final T source, final Procedure1<? super String> validationProcedure) {
      final Pair<Binding, ControlDecoration> bindingAndDecoration = Utils.<T>bindWithValidationDecorationSupport(control, 
        this.dataBindingContext, beanClass, propertyName, source, validationProcedure);
      this.bindings.add(bindingAndDecoration.getKey());
      this.decorations.add(bindingAndDecoration.getValue());
    }
    
    @Override
    public boolean close() {
      boolean _xblockexpression = false;
      {
        final Consumer<Binding> _function = (Binding it) -> {
          it.dispose();
        };
        this.bindings.forEach(_function);
        this.bindings.clear();
        this.dataBindingContext.dispose();
        final Consumer<ControlDecoration> _function_1 = (ControlDecoration it) -> {
          it.dispose();
        };
        this.decorations.forEach(_function_1);
        this.decorations.clear();
        this.configurationTemplate.removePropertyChangeListener(this);
        _xblockexpression = super.close();
      }
      return _xblockexpression;
    }
    
    @Override
    public boolean isResizable() {
      return true;
    }
    
    @Pure
    protected ProcessingConfigurationWithLaunchConfigurationName getConfigurationTemplate() {
      return this.configurationTemplate;
    }
  }
  
  protected final Shell parentShell;
  
  protected final IProject project;
  
  protected final IFile file;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final String name;
  
  public AbstractLaunchConfigurationTemplate(final Shell parentShell, final String name, final IProject project, final IFile file) {
    this.name = name;
    this.parentShell = parentShell;
    this.project = project;
    this.file = file;
  }
  
  public ProcessingConfigurationWithLaunchConfigurationName extendInitializedProcessingConfigurationTemplate(final ProcessingConfigurationWithLaunchConfigurationName initializedConfiguration) {
    return null;
  }
  
  public abstract Boolean isApplicable(final EObject modelRoot, final Map<String, String> technologyNamePerAlias);
  
  public abstract AbstractLaunchConfigurationTemplate.AbstractTemplateCompletionDialog getCompletionDialog();
  
  @Pure
  public String getName() {
    return this.name;
  }
}
