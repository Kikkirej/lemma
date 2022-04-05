package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui.tab.constraints_table;

import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.Constraint;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.AbstractConstraintType;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeFactory;
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.constraints.types.ConstraintTypeIdentifier;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelection;
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
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class ConstraintsTable {
  private static final AbstractConstraintType DEFAULT_CONSTRAINT_TYPE = ConstraintTypeFactory.fromIdentifier(ConstraintTypeIdentifier.MODEL_KIND);
  
  private static final LocalResourceManager RESOURCE_MANAGER = new LocalResourceManager(JFaceResources.getResources());
  
  private static final Image NEW_IMAGE = LemmaUiUtils.createImage(ConstraintsTable.RESOURCE_MANAGER, ConstraintsTable.class, "add.gif");
  
  private static final Image REMOVE_IMAGE = LemmaUiUtils.createImage(ConstraintsTable.RESOURCE_MANAGER, ConstraintsTable.class, 
    "remove.png");
  
  private final Composite parent;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private final TableViewer viewer;
  
  private Button newButton;
  
  private Button removeButton;
  
  private final List<AbstractConstraintType> supportedConstraintTypes;
  
  private final DataBindingContext dataBindingContext = new DataBindingContext();
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private boolean enabled = true;
  
  private List<Constraint> currentConstraints;
  
  public ConstraintsTable(final Composite parent, final Set<AbstractConstraintType> supportedConstraintTypes, final Consumer<Binding> newBindingCallback, final Consumer<Binding> removedBindingCallback) {
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(supportedConstraintTypes);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException(("Set of supported constraint types must not be " + 
        "empty"));
    }
    final Function1<AbstractConstraintType, String> _function = (AbstractConstraintType it) -> {
      return it.getName();
    };
    this.supportedConstraintTypes = IterableExtensions.<AbstractConstraintType, String>sortBy(IterableExtensions.<AbstractConstraintType>toList(supportedConstraintTypes), _function);
    Composite _composite = new Composite(parent, SWT.NONE);
    this.parent = _composite;
    this.setParentLayout();
    TableViewer _tableViewer = new TableViewer(this.parent);
    this.viewer = _tableViewer;
    ColumnViewerToolTipSupport.enableFor(this.viewer);
    this.viewer.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object inputElement) {
        return ((List<Constraint>) inputElement).toArray();
      }
    });
    this.setViewerLayout();
    this.createTypeColumn();
    this.createValueColumn();
    this.createButtonRow();
  }
  
  private void setParentLayout() {
    GridLayout _gridLayout = new GridLayout(1, false);
    this.parent.setLayout(_gridLayout);
    final GridData parentData = new GridData();
    parentData.grabExcessHorizontalSpace = true;
    parentData.grabExcessVerticalSpace = true;
    parentData.horizontalAlignment = GridData.FILL;
    parentData.verticalAlignment = GridData.FILL;
    this.parent.setLayoutData(parentData);
  }
  
  private void setViewerLayout() {
    Table _table = this.viewer.getTable();
    _table.setHeaderVisible(true);
    Table _table_1 = this.viewer.getTable();
    _table_1.setLinesVisible(true);
    final GridData layoutData = new GridData();
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.horizontalAlignment = GridData.FILL;
    layoutData.grabExcessVerticalSpace = true;
    layoutData.verticalAlignment = GridData.FILL;
    Table _table_2 = this.viewer.getTable();
    _table_2.setLayoutData(layoutData);
  }
  
  private void createTypeColumn() {
    final TableViewerColumn typeColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = typeColumn.getColumn();
    _column.setWidth(150);
    TableColumn _column_1 = typeColumn.getColumn();
    _column_1.setText("Type");
    typeColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Constraint) element).getType().getName();
      }
    });
    TypeColumnEditingSupport _typeColumnEditingSupport = new TypeColumnEditingSupport(this, this.dataBindingContext, 
      this.supportedConstraintTypes);
    typeColumn.setEditingSupport(_typeColumnEditingSupport);
  }
  
  private void createValueColumn() {
    final TableViewerColumn valueColumn = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = valueColumn.getColumn();
    _column.setWidth(300);
    TableColumn _column_1 = valueColumn.getColumn();
    _column_1.setText("Value");
    valueColumn.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Constraint) element).getValue();
      }
      
      @Override
      public Image getImage(final Object element) {
        Image _xtrycatchfinallyexpression = null;
        try {
          Object _xblockexpression = null;
          {
            final Constraint constraint = ((Constraint) element);
            constraint.getType().checkValidValueInUserRepresentation(constraint.getValue());
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
            final Constraint constraint = ((Constraint) element);
            constraint.getType().checkValidValueInUserRepresentation(constraint.getValue());
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
  
  private void createButtonRow() {
    final Composite buttonRow = new Composite(this.parent, SWT.NONE);
    RowLayout _rowLayout = new RowLayout();
    buttonRow.setLayout(_rowLayout);
    this.createNewButton(buttonRow);
    this.createRemoveButton(buttonRow);
  }
  
  private void createNewButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.newButton = _button;
    this.newButton.setImage(ConstraintsTable.NEW_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      final Constraint constraint = new Constraint();
      constraint.setType(ConstraintsTable.DEFAULT_CONSTRAINT_TYPE);
      constraint.setValue("");
      this.currentConstraints.add(constraint);
      this.viewer.setInput(this.currentConstraints);
      this.viewer.refresh();
      this.viewer.editElement(constraint, 1);
    };
    this.newButton.addListener(SWT.Selection, _function);
  }
  
  private void createRemoveButton(final Composite parent) {
    Button _button = new Button(parent, SWT.PUSH);
    this.removeButton = _button;
    this.removeButton.setImage(ConstraintsTable.REMOVE_IMAGE);
    final Listener _function = (Event it) -> {
      Object _input = this.viewer.getInput();
      boolean _tripleEquals = (_input == null);
      if (_tripleEquals) {
        return;
      }
      ISelection _selection = this.viewer.getSelection();
      final IStructuredSelection selection = ((IStructuredSelection) _selection);
      boolean _isEmpty = selection.isEmpty();
      if (_isEmpty) {
        MessageDialog.openError(this.viewer.getControl().getShell(), "No row selected", ("Please select " + 
          "a constraint row for removal"));
        return;
      } else {
        int _length = ((Object[])Conversions.unwrapArray(selection, Object.class)).length;
        boolean _greaterThan = (_length > 1);
        if (_greaterThan) {
          MessageDialog.openError(this.viewer.getControl().getShell(), "Too many rows selected", ("Please " + 
            "select only a single constraint row for removal"));
          return;
        }
      }
      this.currentConstraints.remove(selection.getFirstElement());
      this.viewer.setInput(this.currentConstraints);
    };
    this.removeButton.addListener(SWT.Selection, _function);
  }
  
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
    this.newButton.setEnabled(enabled);
    this.removeButton.setEnabled(enabled);
  }
  
  public void setInput(final List<Constraint> constraints) {
    this.currentConstraints = constraints;
    this.viewer.setInput(constraints);
  }
  
  @Pure
  public TableViewer getViewer() {
    return this.viewer;
  }
  
  @Pure
  public boolean getEnabled() {
    return this.enabled;
  }
}
