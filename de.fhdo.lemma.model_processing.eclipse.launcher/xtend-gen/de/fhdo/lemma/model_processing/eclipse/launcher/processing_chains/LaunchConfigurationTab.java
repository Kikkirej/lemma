package de.fhdo.lemma.model_processing.eclipse.launcher.processing_chains;

import com.google.common.base.Objects;
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table.ArgumentsTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class LaunchConfigurationTab extends AbstractLaunchConfigurationTab implements PropertyChangeListener {
  private static final LocalResourceManager RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources());
  
  private static final Image NEW_IMAGE = LemmaUiUtils.createImage(LaunchConfigurationTab.RESOURCE_MANAGER, LaunchConfigurationTab.class, 
    "add.gif");
  
  private static final Image REMOVE_IMAGE = LemmaUiUtils.createImage(LaunchConfigurationTab.RESOURCE_MANAGER, ArgumentsTable.class, 
    "remove.png");
  
  private static final Image MOVE_UP_IMAGE = LemmaUiUtils.createImage(LaunchConfigurationTab.RESOURCE_MANAGER, ArgumentsTable.class, 
    "moveUp.png");
  
  private static final Image MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(LaunchConfigurationTab.RESOURCE_MANAGER, ArgumentsTable.class, 
    "moveDown.png");
  
  private final DataBindingContext dataBindingContext = new DataBindingContext();
  
  private final Map<String, ILaunchConfiguration> availableLaunchConfigurations = Utils.getProcessingConfigurationLaunchConfigurations();
  
  private Composite mainComposite;
  
  private ProcessingChain originalChain;
  
  private ProcessingChain currentChain;
  
  private TableViewer tableViewer;
  
  private Button newButton;
  
  private Button removeButton;
  
  private Button moveUpButton;
  
  private Button moveDownButton;
  
  @Override
  public void createControl(final Composite parent) {
    Composite _composite = new Composite(parent, SWT.NULL);
    this.mainComposite = _composite;
    GridData _gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    this.mainComposite.setLayoutData(_gridData);
    GridLayout _gridLayout = new GridLayout(1, false);
    this.mainComposite.setLayout(_gridLayout);
    this.setControl(this.mainComposite);
    this.createTable(this.mainComposite);
    this.createButtonRow(this.mainComposite);
  }
  
  private void createTable(final Composite parent) {
    TableViewer _tableViewer = new TableViewer(parent);
    this.tableViewer = _tableViewer;
    this.tableViewer.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object inputElement) {
        return ((Object[])Conversions.unwrapArray(LaunchConfigurationTab.this.currentChain.getEntries(), Object.class));
      }
    });
    Table _table = this.tableViewer.getTable();
    _table.setHeaderVisible(true);
    Table _table_1 = this.tableViewer.getTable();
    _table_1.setLinesVisible(true);
    Table _table_2 = this.tableViewer.getTable();
    GridData _gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
    _table_2.setLayoutData(_gridData);
    this.createLaunchConfigurationColumn();
    this.createExitValueComparatorColumn();
    this.createExitValueColumn();
  }
  
  private void createLaunchConfigurationColumn() {
    final TableViewerColumn column = new TableViewerColumn(this.tableViewer, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(500);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("LEMMA Model Processing Configuration");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((ProcessingChainEntry) element).getLaunchConfigurationName();
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validLaunchConfigurationName(LaunchConfigurationTab.this.availableLaunchConfigurations);
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((Image)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            _xtrycatchfinallyexpression = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
      
      @Override
      public String getToolTipText(final Object element) {
        String _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validLaunchConfigurationName(LaunchConfigurationTab.this.availableLaunchConfigurations);
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((String)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            final IllegalArgumentException ex = (IllegalArgumentException)_t;
            _xtrycatchfinallyexpression = ex.getMessage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
    });
    final Function1<String, String> _function = (String it) -> {
      return it;
    };
    List<String> _sortBy = IterableExtensions.<String, String>sortBy(this.availableLaunchConfigurations.keySet(), _function);
    LaunchConfigurationNameColumnEditingSupport _launchConfigurationNameColumnEditingSupport = new LaunchConfigurationNameColumnEditingSupport(
      this.tableViewer, 
      this.dataBindingContext, _sortBy);
    column.setEditingSupport(_launchConfigurationNameColumnEditingSupport);
  }
  
  private void createExitValueComparatorColumn() {
    final TableViewerColumn column = new TableViewerColumn(this.tableViewer, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(210);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("Previous Exit Value Comparator");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        final PreviousExitValueComparator.Comparator comparator = ((ProcessingChainEntry) element).getPreviousExitValueComparator();
        return PreviousExitValueComparator.getUserRepresentation(comparator);
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validPreviousExitValueComparator();
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((Image)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            _xtrycatchfinallyexpression = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
      
      @Override
      public String getToolTipText(final Object element) {
        String _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validPreviousExitValueComparator();
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((String)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            final IllegalArgumentException ex = (IllegalArgumentException)_t;
            _xtrycatchfinallyexpression = ex.getMessage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
    });
    PreviousExitValueComparatorColumnEditingSupport _previousExitValueComparatorColumnEditingSupport = new PreviousExitValueComparatorColumnEditingSupport(this.tableViewer, 
      this.dataBindingContext);
    column.setEditingSupport(_previousExitValueComparatorColumnEditingSupport);
  }
  
  private void createExitValueColumn() {
    final TableViewerColumn column = new TableViewerColumn(this.tableViewer, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(250);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("Expected Exit Value from the Execution of the Previous Configuration");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        String _elvis = null;
        Integer _previousExitValue = ((ProcessingChainEntry) element).getPreviousExitValue();
        String _string = null;
        if (_previousExitValue!=null) {
          _string=_previousExitValue.toString();
        }
        if (_string != null) {
          _elvis = _string;
        } else {
          _elvis = "";
        }
        return _elvis;
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validPreviousExitValue();
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((Image)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            _xtrycatchfinallyexpression = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
      
      @Override
      public String getToolTipText(final Object element) {
        String _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((ProcessingChainEntry) element).validPreviousExitValue();
            _xblockexpression = null;
          }
          _xtrycatchfinallyexpression = ((String)_xblockexpression);
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            final IllegalArgumentException ex = (IllegalArgumentException)_t;
            _xtrycatchfinallyexpression = ex.getMessage();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
        return _xtrycatchfinallyexpression;
      }
    });
    PreviousExitValueColumnEditingSupport _previousExitValueColumnEditingSupport = new PreviousExitValueColumnEditingSupport(this.tableViewer, 
      this.dataBindingContext);
    column.setEditingSupport(_previousExitValueColumnEditingSupport);
  }
  
  private void createButtonRow(final Composite parent) {
    final Composite buttonRow = new Composite(parent, SWT.NONE);
    RowLayout _rowLayout = new RowLayout();
    buttonRow.setLayout(_rowLayout);
    this.createNewButton(buttonRow);
    this.createRemoveButton(buttonRow);
    this.createMoveUpButton(buttonRow);
    this.createMoveDownButton(buttonRow);
  }
  
  private void createNewButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.newButton = _button;
    this.newButton.setImage(LaunchConfigurationTab.NEW_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.tableViewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      final ProcessingChainEntry entry = new ProcessingChainEntry(
        "", 
        this.currentChain, 
        PreviousExitValueComparator.Comparator.EQUAL, 
        0);
      this.addEntry(entry);
      this.tableViewer.editElement(entry, 0);
    };
    this.newButton.addListener(SWT.Selection, _function);
  }
  
  private void addEntry(final ProcessingChainEntry entry) {
    this.currentChain.addEntry(entry);
    this.syncInput();
  }
  
  private void clearIrrelevantFieldsIfFirstEntryInChain(final ProcessingChainEntry entry) {
    int _indexOf = this.currentChain.getEntries().indexOf(entry);
    boolean _equals = (_indexOf == 0);
    if (_equals) {
      entry.setPreviousExitValueComparator(null);
      entry.setPreviousExitValue(null);
    }
  }
  
  private void syncInput() {
    boolean _isEmpty = this.currentChain.getEntries().isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      this.clearIrrelevantFieldsIfFirstEntryInChain(this.currentChain.getEntries().get(0));
    }
    this.tableViewer.setInput(this.currentChain.getEntries());
  }
  
  private void createRemoveButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.removeButton = _button;
    this.removeButton.setImage(LaunchConfigurationTab.REMOVE_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.tableViewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      final IStructuredSelection selectedEntries = Utils.rowSelectionOrError(this.tableViewer);
      if ((selectedEntries != null)) {
        final Function1<Object, ProcessingChainEntry> _function_1 = (Object it_1) -> {
          return ((ProcessingChainEntry) it_1);
        };
        this.currentChain.removeAll(IterableExtensions.<ProcessingChainEntry>toList(IterableExtensions.<Object, ProcessingChainEntry>map(selectedEntries, _function_1)));
        this.syncInput();
      }
    };
    this.removeButton.addListener(SWT.Selection, _function);
  }
  
  private void createMoveUpButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.moveUpButton = _button;
    this.moveUpButton.setImage(LaunchConfigurationTab.MOVE_UP_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.tableViewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.tableViewer);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Object selectedEntry = _firstElement;
      if ((selectedEntry != null)) {
        this.currentChain.moveUp(((ProcessingChainEntry) selectedEntry));
        this.syncInput();
      }
    };
    this.moveUpButton.addListener(SWT.Selection, _function);
  }
  
  private void createMoveDownButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.moveDownButton = _button;
    this.moveDownButton.setImage(LaunchConfigurationTab.MOVE_DOWN_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.tableViewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.tableViewer);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Object selectedEntry = _firstElement;
      if ((selectedEntry != null)) {
        this.currentChain.moveDown(((ProcessingChainEntry) selectedEntry));
        this.syncInput();
      }
    };
    this.moveDownButton.addListener(SWT.Selection, _function);
  }
  
  @Override
  public void initializeFrom(final ILaunchConfiguration launchConfiguration) {
    ProcessingChain _elvis = null;
    ProcessingChain _deserializeFrom = ProcessingChain.deserializeFrom(launchConfiguration);
    if (_deserializeFrom != null) {
      _elvis = _deserializeFrom;
    } else {
      ProcessingChain _processingChain = new ProcessingChain();
      _elvis = _processingChain;
    }
    final ProcessingChain chain = _elvis;
    this.setCurrentChain(chain);
    this.syncInput();
  }
  
  private void setCurrentChain(final ProcessingChain chain) {
    this.removeCurrentConfigurationBindings();
    if (this.currentChain!=null) {
      this.currentChain.removePropertyChangeListener(this);
    }
    this.originalChain = chain;
    Object _clone = chain.clone();
    this.currentChain = ((ProcessingChain) _clone);
    this.currentChain.addPropertyChangeListener(this);
  }
  
  private void removeCurrentConfigurationBindings() {
    final Consumer<Binding> _function = (Binding it) -> {
      it.dispose();
      this.dataBindingContext.removeBinding(it);
    };
    this.dataBindingContext.getBindings().forEach(_function);
  }
  
  @Override
  public boolean isValid(final ILaunchConfiguration configuration) {
    boolean _xtrycatchfinallyexpression = false;
    try {
      boolean _xblockexpression = false;
      {
        this.currentChain.validate(this.availableLaunchConfigurations);
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
  public String getErrorMessage() {
    String _xtrycatchfinallyexpression = null;
    try {
      Object _xblockexpression = null;
      {
        this.currentChain.validate(this.availableLaunchConfigurations, true);
        _xblockexpression = null;
      }
      _xtrycatchfinallyexpression = ((String)_xblockexpression);
    } catch (final Throwable _t) {
      if (_t instanceof IllegalArgumentException) {
        final IllegalArgumentException ex = (IllegalArgumentException)_t;
        _xtrycatchfinallyexpression = ex.getMessage();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  @Override
  public boolean isDirty() {
    return (!Objects.equal(this.originalChain, this.currentChain));
  }
  
  @Override
  public void performApply(final ILaunchConfigurationWorkingCopy launchConfiguration) {
    ProcessingChain.setProcessingChainAsAttribute(launchConfiguration, this.currentChain);
  }
  
  @Override
  public void propertyChange(final PropertyChangeEvent event) {
    this.updateLaunchConfigurationDialog();
  }
  
  @Override
  public String getName() {
    return "Chained LEMMA Model Processing Configurations";
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
    this.dataBindingContext.dispose();
    super.dispose();
  }
}
