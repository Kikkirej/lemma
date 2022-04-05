package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.shortcut;

import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.PreviousExitValueComparator;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains.ProcessingChainEntry;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class LaunchConfigurationChainCreationDialog extends TitleAreaDialog implements PropertyChangeListener {
  private static final LocalResourceManager RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources());
  
  private static final Image MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(
    LaunchConfigurationChainCreationDialog.RESOURCE_MANAGER, LaunchConfigurationChainCreationDialog.class, 
    "moveDown.png");
  
  private static final Image MOVE_RIGHT_IMAGE = LemmaUiUtils.createImage(
    LaunchConfigurationChainCreationDialog.RESOURCE_MANAGER, LaunchConfigurationChainCreationDialog.class, 
    "moveRight.png");
  
  private static final Image MOVE_UP_IMAGE = LemmaUiUtils.createImage(
    LaunchConfigurationChainCreationDialog.RESOURCE_MANAGER, LaunchConfigurationChainCreationDialog.class, 
    "moveUp.png");
  
  private static final Image REMOVE_IMAGE = LemmaUiUtils.createImage(
    LaunchConfigurationChainCreationDialog.RESOURCE_MANAGER, LaunchConfigurationChainCreationDialog.class, 
    "remove.png");
  
  private final Map<String, ILaunchConfiguration> availableLaunchConfigurations;
  
  private final ProcessingChainWithLaunchConfigurationName chainTemplate;
  
  private TableViewer launchConfigurationsTable;
  
  private TableViewer chainTable;
  
  private Button storeButton;
  
  private Button runButton;
  
  private final DataBindingContext dataBindingContext = new DataBindingContext();
  
  private final ArrayList<ControlDecoration> currentDecorations = CollectionLiterals.<ControlDecoration>newArrayList();
  
  private final Procedure2<? super ProcessingChainWithLaunchConfigurationName, ? super Map<String, ILaunchConfiguration>> runCallback;
  
  private final Procedure2<? super ProcessingChainWithLaunchConfigurationName, ? super Map<String, ILaunchConfiguration>> storeCallback;
  
  public LaunchConfigurationChainCreationDialog(final Shell parentShell, final Map<String, ILaunchConfiguration> availableLaunchConfigurations, final ProcessingChainWithLaunchConfigurationName chainTemplate, final Procedure2<? super ProcessingChainWithLaunchConfigurationName, ? super Map<String, ILaunchConfiguration>> runCallback, final Procedure2<? super ProcessingChainWithLaunchConfigurationName, ? super Map<String, ILaunchConfiguration>> storeCallback) {
    super(parentShell);
    this.setHelpAvailable(false);
    this.availableLaunchConfigurations = availableLaunchConfigurations;
    this.chainTemplate = chainTemplate;
    this.runCallback = runCallback;
    this.storeCallback = storeCallback;
    chainTemplate.setLaunchConfigurationName(LaunchConfigurationConstants.LAUNCH_MANAGER.generateLaunchConfigurationName("LEMMA_Model_Processing_Chain"));
    chainTemplate.addPropertyChangeListener(this);
  }
  
  @Override
  public void create() {
    super.create();
    this.setTitle("Create and Run a LEMMA Model Processing Chain");
    this.setMessage(("Use this dialog to assemble a LEMMA Model Processing Chain that executes " + 
      "launch configurations for LEMMA Model Processors in a given order"));
  }
  
  @Override
  public Control createDialogArea(final Composite parent) {
    Control _createDialogArea = super.createDialogArea(parent);
    final Composite container = ((Composite) _createDialogArea);
    GridLayout _gridLayout = new GridLayout(3, false);
    container.setLayout(_gridLayout);
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    container.setLayoutData(_gridData);
    this.createConfigurationNameTextField(container);
    this.createApplicableLaunchConfigurationsTable(container);
    this.createMoveButton(container);
    this.createChainTableAndButtons(container);
    return container;
  }
  
  private boolean createConfigurationNameTextField(final Composite parent) {
    boolean _xblockexpression = false;
    {
      final Composite container = new Composite(parent, SWT.NULL);
      GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
      layoutData.horizontalSpan = 3;
      container.setLayoutData(layoutData);
      GridLayout _gridLayout = new GridLayout(3, false);
      container.setLayout(_gridLayout);
      Label _label = new Label(container, SWT.NULL);
      _label.setText("Launch configuration name:");
      final Text textField = new Text(container, SWT.BORDER);
      GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
      layoutData = _gridData;
      layoutData.horizontalSpan = 2;
      textField.setLayoutData(layoutData);
      final Procedure1<String> _function = (String it) -> {
        ProcessingChainWithLaunchConfigurationName.validLaunchConfigurationName(it);
      };
      final Pair<Binding, ControlDecoration> bindingAndDecoration = Utils.<ProcessingChainWithLaunchConfigurationName>bindWithValidationDecorationSupport(textField, 
        this.dataBindingContext, 
        ProcessingChainWithLaunchConfigurationName.class, 
        "launchConfigurationName", 
        this.chainTemplate, _function);
      _xblockexpression = this.currentDecorations.add(bindingAndDecoration.getValue());
    }
    return _xblockexpression;
  }
  
  private void createApplicableLaunchConfigurationsTable(final Composite parent) {
    final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
    group.setText("Applicable Launch Configurations");
    GridLayout _gridLayout = new GridLayout(1, false);
    group.setLayout(_gridLayout);
    GridData _gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
    group.setLayoutData(_gridData);
    TableViewer _tableViewer = new TableViewer(group);
    this.launchConfigurationsTable = _tableViewer;
    this.launchConfigurationsTable.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object inputElement) {
        return ((Object[])Conversions.unwrapArray(IterableExtensions.<String>sort(LaunchConfigurationChainCreationDialog.this.availableLaunchConfigurations.keySet()), Object.class));
      }
    });
    Table _table = this.launchConfigurationsTable.getTable();
    _table.setHeaderVisible(true);
    Table _table_1 = this.launchConfigurationsTable.getTable();
    _table_1.setLinesVisible(true);
    Table _table_2 = this.launchConfigurationsTable.getTable();
    GridData _gridData_1 = new GridData(GridData.FILL, GridData.FILL, 
      true, true);
    _table_2.setLayoutData(_gridData_1);
    final TableViewerColumn column = new TableViewerColumn(this.launchConfigurationsTable, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(300);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("LEMMA Model Processor Launch Configuration");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((String) element);
      }
    });
    this.launchConfigurationsTable.setInput(this.availableLaunchConfigurations);
  }
  
  private void createMoveButton(final Composite parent) {
    final Composite container = new Composite(parent, SWT.NONE);
    GridLayout _gridLayout = new GridLayout(1, false);
    container.setLayout(_gridLayout);
    GridData _gridData = new GridData(SWT.CENTER, SWT.CENTER, false, false);
    container.setLayoutData(_gridData);
    final Button button = new Button(container, SWT.PUSH);
    button.setImage(LaunchConfigurationChainCreationDialog.MOVE_RIGHT_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.launchConfigurationsTable.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      ISelection _selection = this.launchConfigurationsTable.getSelection();
      final IStructuredSelection selection = ((IStructuredSelection) _selection);
      boolean _isEmpty = selection.isEmpty();
      if (_isEmpty) {
        MessageDialog.openError(this.getShell(), "No Launch Configurations Selected", (("Please " + 
          "select one or more entries from the list of available LEMMA Model Processor ") + 
          "launch configurations"));
        return;
      }
      final Consumer<Object> _function_1 = (Object it_1) -> {
        ProcessingChainEntry _processingChainEntry = new ProcessingChainEntry(((String) it_1), 
          this.chainTemplate, 
          PreviousExitValueComparator.Comparator.EQUAL, 
          0);
        this.chainTemplate.addEntry(_processingChainEntry);
      };
      selection.forEach(_function_1);
      this.syncChainTable();
    };
    button.addListener(SWT.Selection, _function);
  }
  
  private void syncChainTable() {
    this.chainTable.setInput(this.chainTemplate.getEntries());
  }
  
  private void createChainTableAndButtons(final Composite parent) {
    final Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
    group.setText("Launch Configuration Chain Entries");
    GridLayout _gridLayout = new GridLayout(1, false);
    group.setLayout(_gridLayout);
    GridData _gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
    group.setLayoutData(_gridData);
    TableViewer _tableViewer = new TableViewer(group);
    this.chainTable = _tableViewer;
    this.chainTable.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object element) {
        return ((Object[])Conversions.unwrapArray(((List<ProcessingChainEntry>) element), Object.class));
      }
    });
    Table _table = this.chainTable.getTable();
    _table.setHeaderVisible(true);
    Table _table_1 = this.chainTable.getTable();
    _table_1.setLinesVisible(true);
    Table _table_2 = this.chainTable.getTable();
    GridData _gridData_1 = new GridData(GridData.FILL, GridData.FILL, true, true);
    _table_2.setLayoutData(_gridData_1);
    final TableViewerColumn column = new TableViewerColumn(this.chainTable, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(300);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("Launch Configuration Chain Entry");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((ProcessingChainEntry) element).getLaunchConfigurationName();
      }
    });
    this.createChainTableButtons(group);
  }
  
  private void createChainTableButtons(final Composite parent) {
    final Composite buttonRow = new Composite(parent, SWT.NONE);
    RowLayout _rowLayout = new RowLayout();
    buttonRow.setLayout(_rowLayout);
    this.createRemoveButton(buttonRow);
    this.createMoveUpButton(buttonRow);
    this.createMoveDownButton(buttonRow);
  }
  
  private void createRemoveButton(final Composite parent) {
    final Button button = new Button(parent, SWT.PUSH);
    button.setImage(LaunchConfigurationChainCreationDialog.REMOVE_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.chainTable.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      final IStructuredSelection selectedEntries = Utils.rowSelectionOrError(this.chainTable);
      if ((selectedEntries != null)) {
        final Function1<Object, ProcessingChainEntry> _function_1 = (Object it_1) -> {
          return ((ProcessingChainEntry) it_1);
        };
        this.chainTemplate.removeAll(IterableExtensions.<ProcessingChainEntry>toList(IterableExtensions.<Object, ProcessingChainEntry>map(selectedEntries, _function_1)));
        this.syncChainTable();
      }
    };
    button.addListener(SWT.Selection, _function);
  }
  
  private void createMoveUpButton(final Composite parent) {
    final Button button = new Button(parent, SWT.PUSH);
    button.setImage(LaunchConfigurationChainCreationDialog.MOVE_UP_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.chainTable.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.chainTable);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Object selectedEntry = _firstElement;
      if ((selectedEntry != null)) {
        this.chainTemplate.moveUp(((ProcessingChainEntry) selectedEntry));
        this.syncChainTable();
      }
    };
    button.addListener(SWT.Selection, _function);
  }
  
  private void createMoveDownButton(final Composite parent) {
    final Button button = new Button(parent, SWT.PUSH);
    button.setImage(LaunchConfigurationChainCreationDialog.MOVE_DOWN_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.chainTable.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.chainTable);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Object selectedEntry = _firstElement;
      if ((selectedEntry != null)) {
        this.chainTemplate.moveDown(((ProcessingChainEntry) selectedEntry));
        this.syncChainTable();
      }
    };
    button.addListener(SWT.Selection, _function);
  }
  
  @Override
  public void createButtonsForButtonBar(final Composite parent) {
    this.createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    this.storeButton = this.createButton(parent, IDialogConstants.FINISH_ID, "Store Without Running", 
      false);
    this.runButton = this.createButton(parent, IDialogConstants.OK_ID, "Run", true);
    this.updateDialogFromChainValidity();
  }
  
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    this.updateDialogFromChainValidity();
  }
  
  private void updateDialogFromChainValidity() {
    final boolean chainIsValid = this.isValid(this.chainTemplate);
    this.storeButton.setEnabled(chainIsValid);
    this.runButton.setEnabled(chainIsValid);
  }
  
  private boolean isValid(final ProcessingChainWithLaunchConfigurationName chain) {
    boolean _xtrycatchfinallyexpression = false;
    try {
      boolean _xblockexpression = false;
      {
        chain.validate(this.availableLaunchConfigurations);
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
  
  @Override
  public int open() {
    boolean _isEmpty = this.availableLaunchConfigurations.isEmpty();
    if (_isEmpty) {
      MessageDialog.openError(this.getShell(), "No Launch Configurations", ("No LEMMA Model Processor " + 
        "launch configurations found."));
      return Window.CANCEL;
    }
    return super.open();
  }
  
  @Override
  public void buttonPressed(final int buttonId) {
    if ((buttonId == IDialogConstants.FINISH_ID)) {
      this.storePressed();
    } else {
      super.buttonPressed(buttonId);
    }
  }
  
  private void storePressed() {
    this.storeCallback.apply(this.chainTemplate, this.availableLaunchConfigurations);
    super.okPressed();
  }
  
  @Override
  public void okPressed() {
    this.runCallback.apply(this.chainTemplate, this.availableLaunchConfigurations);
    super.okPressed();
  }
  
  @Override
  public boolean close() {
    boolean _xblockexpression = false;
    {
      this.dataBindingContext.dispose();
      final Consumer<ControlDecoration> _function = (ControlDecoration it) -> {
        it.dispose();
      };
      this.currentDecorations.forEach(_function);
      this.chainTemplate.removePropertyChangeListener(this);
      _xblockexpression = super.close();
    }
    return _xblockexpression;
  }
  
  @Override
  public boolean isResizable() {
    return true;
  }
  
  @Override
  public Point getInitialSize() {
    return new Point(950, 500);
  }
}
