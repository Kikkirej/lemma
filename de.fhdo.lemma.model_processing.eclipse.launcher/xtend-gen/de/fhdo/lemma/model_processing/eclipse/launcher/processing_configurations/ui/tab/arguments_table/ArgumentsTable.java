package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.arguments_table;

import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Arguments;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.AbstractArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.ArgumentKindFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.kinds.SingleValuedParameterArgumentKind;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.AbstractArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FileArgumentType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.FolderArgumentType;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ArgumentsTable {
  private static final AbstractArgumentKind DEFAULT_ARGUMENT_KIND = ArgumentKindFactory.fromIdentifier(SingleValuedParameterArgumentKind.IDENTIFIER);
  
  private static final LocalResourceManager RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources());
  
  private static final Image NEW_IMAGE = LemmaUiUtils.createImage(ArgumentsTable.RESOURCE_MANAGER, ArgumentsTable.class, "add.gif");
  
  private static final Image REMOVE_IMAGE = LemmaUiUtils.createImage(ArgumentsTable.RESOURCE_MANAGER, ArgumentsTable.class, 
    "remove.png");
  
  private static final Image MOVE_UP_IMAGE = LemmaUiUtils.createImage(ArgumentsTable.RESOURCE_MANAGER, ArgumentsTable.class, 
    "moveUp.png");
  
  private static final Image MOVE_DOWN_IMAGE = LemmaUiUtils.createImage(ArgumentsTable.RESOURCE_MANAGER, ArgumentsTable.class, 
    "moveDown.png");
  
  private static final HashMap<Argument, Button> INPUT_SUPPORT_BUTTONS = CollectionLiterals.<Argument, Button>newHashMap();
  
  private final Map<Class<? extends AbstractArgumentType>, Consumer<Argument>> TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT;
  
  private final Composite parent;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final TableViewer viewer;
  
  private Button newButton;
  
  private Button removeButton;
  
  private Button moveUpButton;
  
  private Button moveDownButton;
  
  private final List<AbstractArgumentKind> supportedArgumentKinds;
  
  private final DataBindingContext dataBindingContext = new DataBindingContext();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private boolean enabled = true;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private ProcessingConfiguration currentConfiguration;
  
  private Arguments currentArguments;
  
  public ArgumentsTable(final Composite parent, final Set<AbstractArgumentKind> supportedArgumentKinds) {
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(supportedArgumentKinds);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException("Set of supported argument kinds must not be empty");
    }
    this.parent = parent;
    final Function1<AbstractArgumentKind, String> _function = (AbstractArgumentKind it) -> {
      return it.getName();
    };
    this.supportedArgumentKinds = IterableExtensions.<AbstractArgumentKind, String>sortBy(IterableExtensions.<AbstractArgumentKind>toList(supportedArgumentKinds), _function);
    TableViewer _tableViewer = new TableViewer(parent);
    this.viewer = _tableViewer;
    ColumnViewerToolTipSupport.enableFor(this.viewer);
    this.viewer.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object inputElement) {
        return ((Object[])Conversions.unwrapArray(ArgumentsTable.this.currentArguments.get(), Object.class));
      }
    });
    this.setViewerLayout();
    this.createKindColumn();
    this.createTypeColumn();
    this.createParameterColumn();
    this.createValueColumn();
    Pair<Class<FileArgumentType>, Consumer<Argument>> _mappedTo = Pair.<Class<FileArgumentType>, Consumer<Argument>>of(FileArgumentType.class, new Consumer<Argument>() {
      @Override
      public void accept(final Argument argument) {
        Shell _activeShell = ArgumentsTable.this.viewer.getControl().getDisplay().getActiveShell();
        final FileDialog fileDialog = new FileDialog(_activeShell, SWT.OPEN);
        fileDialog.setText("Select a file");
        final String selectedFile = fileDialog.open();
        if ((selectedFile != null)) {
          argument.setValue(selectedFile);
        }
      }
    });
    Pair<Class<FolderArgumentType>, Consumer<Argument>> _mappedTo_1 = Pair.<Class<FolderArgumentType>, Consumer<Argument>>of(FolderArgumentType.class, new Consumer<Argument>() {
      @Override
      public void accept(final Argument argument) {
        Shell _activeShell = ArgumentsTable.this.viewer.getControl().getDisplay().getActiveShell();
        final DirectoryDialog folderDialog = new DirectoryDialog(_activeShell, 
          SWT.OPEN);
        folderDialog.setText("Select a folder");
        final String selectedFolder = folderDialog.open();
        if ((selectedFolder != null)) {
          argument.setValue(selectedFolder);
        }
      }
    });
    this.TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT = Collections.<Class<? extends AbstractArgumentType>, Consumer<Argument>>unmodifiableMap(CollectionLiterals.<Class<? extends AbstractArgumentType>, Consumer<Argument>>newHashMap(_mappedTo, _mappedTo_1));
    this.createInputSupportColumn();
    this.createButtonRow();
  }
  
  private void setViewerLayout() {
    Table _table = this.viewer.getTable();
    _table.setHeaderVisible(true);
    Table _table_1 = this.viewer.getTable();
    _table_1.setLinesVisible(true);
    Table _table_2 = this.viewer.getTable();
    GridData _gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
    _table_2.setLayoutData(_gridData);
  }
  
  private void createKindColumn() {
    final TableViewerColumn kindColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = kindColumn.getColumn();
    _column.setWidth(150);
    TableColumn _column_1 = kindColumn.getColumn();
    _column_1.setText("Kind");
    kindColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Argument) element).getKind().getName();
      }
    });
    KindColumnEditingSupport _kindColumnEditingSupport = new KindColumnEditingSupport(this, this.dataBindingContext, 
      this.supportedArgumentKinds);
    kindColumn.setEditingSupport(_kindColumnEditingSupport);
  }
  
  private void createTypeColumn() {
    final TableViewerColumn typeColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = typeColumn.getColumn();
    _column.setWidth(200);
    TableColumn _column_1 = typeColumn.getColumn();
    _column_1.setText("Type");
    typeColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Argument) element).getType().getName();
      }
    });
    TypeColumnEditingSupport _typeColumnEditingSupport = new TypeColumnEditingSupport(this, this.dataBindingContext);
    typeColumn.setEditingSupport(_typeColumnEditingSupport);
  }
  
  private void createParameterColumn() {
    final TableViewerColumn parameterColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = parameterColumn.getColumn();
    _column.setWidth(200);
    TableColumn _column_1 = parameterColumn.getColumn();
    _column_1.setText("Parameter");
    parameterColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Argument) element).getParameter();
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            ((Argument) element).validParameter();
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
            ((Argument) element).validParameter();
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
    ParameterColumnEditingSupport _parameterColumnEditingSupport = new ParameterColumnEditingSupport(this, this.dataBindingContext);
    parameterColumn.setEditingSupport(_parameterColumnEditingSupport);
  }
  
  private void createValueColumn() {
    final TableViewerColumn valueColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = valueColumn.getColumn();
    _column.setWidth(830);
    TableColumn _column_1 = valueColumn.getColumn();
    _column_1.setText("Value");
    valueColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Argument) element).getValue();
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            final Argument argument = ((Argument) element);
            argument.getType().checkValidValueInUserRepresentation(ArgumentsTable.this.currentConfiguration, 
              argument.getValue());
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
            final Argument argument = ((Argument) element);
            argument.getType().checkValidValueInUserRepresentation(ArgumentsTable.this.currentConfiguration, 
              argument.getValue());
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
    ValueColumnEditingSupport _valueColumnEditingSupport = new ValueColumnEditingSupport(this, this.dataBindingContext);
    valueColumn.setEditingSupport(_valueColumnEditingSupport);
  }
  
  private void createInputSupportColumn() {
    final TableViewerColumn inputSupportColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = inputSupportColumn.getColumn();
    _column.setWidth(20);
    inputSupportColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public void update(final ViewerCell cell) {
        Object _element = cell.getElement();
        final Argument argument = ((Argument) _element);
        Button _get = ArgumentsTable.INPUT_SUPPORT_BUTTONS.get(argument);
        if (_get!=null) {
          _get.dispose();
        }
        Control _control = ArgumentsTable.this.viewer.getControl();
        final Button inputSupportButton = ArgumentsTable.this.putInputSupportButtonIfProvided(ArgumentsTable.INPUT_SUPPORT_BUTTONS, 
          ((Composite) _control), argument);
        Widget _item = cell.getItem();
        final TableItem item = ((TableItem) _item);
        Table _parent = item.getParent();
        final TableEditor editor = new TableEditor(_parent);
        editor.grabHorizontal = true;
        editor.grabVertical = true;
        editor.setEditor(inputSupportButton, item, cell.getColumnIndex());
        editor.layout();
      }
      
      @Override
      public void dispose(final ColumnViewer viewer, final ViewerColumn column) {
        final Consumer<Button> _function = (Button it) -> {
          it.dispose();
        };
        ArgumentsTable.INPUT_SUPPORT_BUTTONS.values().forEach(_function);
        super.dispose();
      }
    });
  }
  
  private Button putInputSupportButtonIfProvided(final Map<Argument, Button> buttonsPerArgument, final Composite parent, final Argument argument) {
    final Consumer<Argument> inputSupport = this.TYPE_SPECIFIC_ARGUMENT_VALUE_INPUT_SUPPORT.get(argument.getType().getClass());
    if ((inputSupport == null)) {
      return null;
    }
    final Button inputSupportButton = new Button(parent, SWT.NONE);
    GridData _gridData = new GridData(SWT.CENTER, SWT.FILL, false, true);
    inputSupportButton.setLayoutData(_gridData);
    inputSupportButton.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL).getImage());
    final Listener _function = (Event it) -> {
      inputSupport.accept(argument);
      this.viewer.update(argument, null);
    };
    inputSupportButton.addListener(SWT.Selection, _function);
    buttonsPerArgument.put(argument, inputSupportButton);
    return inputSupportButton;
  }
  
  private void createButtonRow() {
    final Composite buttonRow = new Composite(this.parent, SWT.NONE);
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
    this.newButton.setImage(ArgumentsTable.NEW_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      AbstractArgumentType _firstSupportedArgumentType = ArgumentsTable.DEFAULT_ARGUMENT_KIND.firstSupportedArgumentType();
      final Argument argument = new Argument(
        ArgumentsTable.DEFAULT_ARGUMENT_KIND, _firstSupportedArgumentType, 
        "", 
        "");
      this.addArgument(argument);
      this.viewer.editElement(argument, 1);
    };
    this.newButton.addListener(SWT.Selection, _function);
  }
  
  public boolean containsArgument(final Argument argument) {
    return this.currentArguments.contains(argument);
  }
  
  public void updateArguments(final Class<?> argumentTypeClass) {
    final List<Argument> tableArguments = this.currentArguments.getManagedArgumentRepresentations(argumentTypeClass);
    final Consumer<Argument> _function = (Argument it) -> {
      this.viewer.update(it, null);
    };
    tableArguments.forEach(_function);
  }
  
  public void addArgument(final Argument argument) {
    this.currentArguments.add(argument);
    this.syncInput();
  }
  
  private void syncInput() {
    this.viewer.setInput(this.currentArguments.get());
  }
  
  public void addArgument(final Argument argument, final int index) {
    this.currentArguments.add(index, argument);
    this.syncInput();
  }
  
  private void createRemoveButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.removeButton = _button;
    this.removeButton.setImage(ArgumentsTable.REMOVE_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      final IStructuredSelection selectedArguments = Utils.rowSelectionOrError(this.viewer);
      if ((selectedArguments != null)) {
        final Function1<Object, Argument> _function_1 = (Object it_1) -> {
          return ((Argument) it_1);
        };
        this.currentArguments.removeAll(IterableExtensions.<Argument>toList(IterableExtensions.<Object, Argument>map(selectedArguments, _function_1)));
        final Consumer<Object> _function_2 = (Object it_1) -> {
          Button _remove = ArgumentsTable.INPUT_SUPPORT_BUTTONS.remove(it_1);
          if (_remove!=null) {
            _remove.dispose();
          }
        };
        selectedArguments.forEach(_function_2);
        this.syncInput();
      }
    };
    this.removeButton.addListener(SWT.Selection, _function);
  }
  
  private void createMoveUpButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.moveUpButton = _button;
    this.moveUpButton.setImage(ArgumentsTable.MOVE_UP_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.viewer);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Argument selectedArgument = ((Argument) _firstElement);
      if ((selectedArgument != null)) {
        this.currentArguments.moveUp(selectedArgument);
        this.syncInput();
      }
    };
    this.moveUpButton.addListener(SWT.Selection, _function);
  }
  
  private void createMoveDownButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.moveDownButton = _button;
    this.moveDownButton.setImage(ArgumentsTable.MOVE_DOWN_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.viewer);
      Object _firstElement = null;
      if (_singleRowSelectionOrError!=null) {
        _firstElement=_singleRowSelectionOrError.getFirstElement();
      }
      final Argument selectedArgument = ((Argument) _firstElement);
      if ((selectedArgument != null)) {
        this.currentArguments.moveDown(selectedArgument);
        this.syncInput();
      }
    };
    this.moveDownButton.addListener(SWT.Selection, _function);
  }
  
  /**
   * def setEnabled(boolean enabled) {
   * this.enabled = enabled
   * newButton.enabled = enabled
   * removeButton.enabled = enabled
   * moveUpButton.enabled = enabled
   * moveDownButton.enabled = enabled
   * }
   */
  public void setInput(final ProcessingConfiguration configuration) {
    this.currentConfiguration = configuration;
    ArrayList<Argument> _elvis = null;
    ArrayList<Argument> _arguments = this.currentConfiguration.getArguments();
    if (_arguments != null) {
      _elvis = _arguments;
    } else {
      ArrayList<Argument> _newArrayList = CollectionLiterals.<Argument>newArrayList();
      _elvis = _newArrayList;
    }
    Arguments _arguments_1 = new Arguments(_elvis);
    this.currentArguments = _arguments_1;
    this.syncInput();
  }
  
  public void addPropertyChangeListener(final PropertyChangeListener listener) {
    if (this.currentArguments!=null) {
      this.currentArguments.addPropertyChangeListener(listener);
    }
  }
  
  public void removePropertyChangeListener(final PropertyChangeListener listener) {
    if (this.currentArguments!=null) {
      this.currentArguments.removePropertyChangeListener(listener);
    }
  }
  
  public void dispose() {
    this.dataBindingContext.dispose();
    this.newButton.dispose();
    this.removeButton.dispose();
    this.moveUpButton.dispose();
    this.moveDownButton.dispose();
    this.viewer.getControl().dispose();
    this.parent.dispose();
  }
  
  @Pure
  public TableViewer getViewer() {
    return this.viewer;
  }
  
  @Pure
  public boolean getEnabled() {
    return this.enabled;
  }
  
  @Pure
  public ProcessingConfiguration getCurrentConfiguration() {
    return this.currentConfiguration;
  }
}
