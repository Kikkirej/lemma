package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui;

import de.fhdo.lemma.model_processing.eclipse.launcher.Utils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class DockerImageSelectionDialog extends Dialog {
  private static final String IMAGE_TAG_SEPARATOR = ":";
  
  private final ArrayList<Pair<String, String>> images = CollectionLiterals.<Pair<String, String>>newArrayList();
  
  private final ArrayList<String> nonParseableImageQueryResults = CollectionLiterals.<String>newArrayList();
  
  private TableViewer viewer;
  
  @Accessors(AccessorType.PUBLIC_GETTER)
  private String selectedImage = null;
  
  public DockerImageSelectionDialog(final Shell parentShell, final String basicDockerCommand) {
    super(parentShell);
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(basicDockerCommand);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException(("Docker selection dialog requires basic Docker " + 
        "command but none was given"));
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(basicDockerCommand);
    _builder.append(" image ls --format ");
    String _plus = (_builder.toString() + 
      "\'{{.Repository}}");
    String _plus_1 = (_plus + DockerImageSelectionDialog.IMAGE_TAG_SEPARATOR);
    final String imageQueryCommand = (_plus_1 + "{{.Tag}}\'");
    Pair<Integer, String> _xtrycatchfinallyexpression = null;
    try {
      _xtrycatchfinallyexpression = Utils.executeShellCommandBlocking(imageQueryCommand, 100, 10);
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Invalid query command \"");
        _builder_1.append(imageQueryCommand);
        _builder_1.append("\"");
        String _plus_2 = ("Available Docker images not determinable: " + _builder_1);
        throw new IllegalArgumentException(_plus_2);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    final Pair<Integer, String> imageQueryResult = _xtrycatchfinallyexpression;
    Integer _key = imageQueryResult.getKey();
    boolean _notEquals = ((_key).intValue() != 0);
    if (_notEquals) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("Command \"");
      _builder_1.append(imageQueryCommand);
      _builder_1.append("\" returned with exit code ");
      String _plus_2 = ("Available Docker images not determinable. " + _builder_1);
      StringConcatenation _builder_2 = new StringConcatenation();
      Integer _key_1 = imageQueryResult.getKey();
      _builder_2.append(_key_1);
      _builder_2.append(": ");
      String _value = imageQueryResult.getValue();
      _builder_2.append(_value);
      String _plus_3 = (_plus_2 + _builder_2);
      throw new IllegalArgumentException(_plus_3);
    }
    final Consumer<String> _function = (String it) -> {
      final String result = StringUtils.removeEnd(StringUtils.removeStart(it, "\'"), "\'");
      final String image = StringUtils.substringBeforeLast(result, DockerImageSelectionDialog.IMAGE_TAG_SEPARATOR);
      final String tag = StringUtils.substringAfterLast(result, DockerImageSelectionDialog.IMAGE_TAG_SEPARATOR);
      if (((!StringExtensions.isNullOrEmpty(image)) && (!StringExtensions.isNullOrEmpty(tag)))) {
        Pair<String, String> _mappedTo = Pair.<String, String>of(image, tag);
        this.images.add(_mappedTo);
      } else {
        this.nonParseableImageQueryResults.add(it);
      }
    };
    imageQueryResult.getValue().lines().sorted().forEach(_function);
  }
  
  @Override
  public Control createDialogArea(final Composite parent) {
    Control _createDialogArea = super.createDialogArea(parent);
    final Composite container = ((Composite) _createDialogArea);
    TableViewer _tableViewer = new TableViewer(container);
    this.viewer = _tableViewer;
    this.viewer.setContentProvider(new IStructuredContentProvider() {
      @Override
      public Object[] getElements(final Object inputElement) {
        return ((Object[])Conversions.unwrapArray(DockerImageSelectionDialog.this.images, Object.class));
      }
    });
    final IDoubleClickListener _function = (DoubleClickEvent it) -> {
      ISelection _selection = it.getSelection();
      final IStructuredSelection selection = ((IStructuredSelection) _selection);
      Object _firstElement = null;
      if (selection!=null) {
        _firstElement=selection.getFirstElement();
      }
      final Pair<String, String> imageAndTag = ((Pair<String, String>) _firstElement);
      if ((imageAndTag != null)) {
        this.okPressedFor(imageAndTag);
      }
    };
    this.viewer.addDoubleClickListener(_function);
    this.setViewerLayout();
    this.createImageColumn();
    this.createTagColumn();
    this.viewer.setInput(this.images);
    return container;
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
  
  private void createImageColumn() {
    final TableViewerColumn column = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(300);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("Image");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Pair<String, String>) element).getKey();
      }
    });
  }
  
  private void createTagColumn() {
    final TableViewerColumn column = new TableViewerColumn(this.viewer, SWT.NONE);
    TableColumn _column = column.getColumn();
    _column.setWidth(150);
    TableColumn _column_1 = column.getColumn();
    _column_1.setText("Tag");
    column.setLabelProvider(new ColumnLabelProvider() {
      @Override
      public String getText(final Object element) {
        return ((Pair<String, String>) element).getValue();
      }
    });
  }
  
  @Override
  public int open() {
    boolean _isEmpty = this.images.isEmpty();
    if (_isEmpty) {
      MessageDialog.openError(this.getShell(), "No Docker Images", "No local Docker images found");
      return Window.CANCEL;
    }
    boolean _isEmpty_1 = this.nonParseableImageQueryResults.isEmpty();
    boolean _not = (!_isEmpty_1);
    if (_not) {
      Shell _shell = this.getShell();
      final Function1<String, String> _function = (String it) -> {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("\\t- ");
        _builder.append(it);
        return _builder.toString();
      };
      String _join = IterableExtensions.join(ListExtensions.<String, String>map(this.nonParseableImageQueryResults, _function), "\n");
      String _plus = (("The image or tag " + 
        "names of the following local Docker images could not be parsed:\n") + _join);
      String _plus_1 = (_plus + "\nThey will be ");
      String _plus_2 = (_plus_1 + 
        "missing in the list of selectable images");
      MessageDialog.openWarning(_shell, "Non-Parseable Docker Images", _plus_2);
    }
    return super.open();
  }
  
  @Override
  public void okPressed() {
    IStructuredSelection _singleRowSelectionOrError = Utils.singleRowSelectionOrError(this.viewer);
    Object _firstElement = null;
    if (_singleRowSelectionOrError!=null) {
      _firstElement=_singleRowSelectionOrError.getFirstElement();
    }
    final Pair<String, String> selectedRow = ((Pair<String, String>) _firstElement);
    if ((selectedRow != null)) {
      this.okPressedFor(selectedRow);
    }
  }
  
  private void okPressedFor(final Pair<String, String> imageAndTag) {
    StringConcatenation _builder = new StringConcatenation();
    String _key = imageAndTag.getKey();
    _builder.append(_key);
    _builder.append(":");
    String _value = imageAndTag.getValue();
    _builder.append(_value);
    this.selectedImage = _builder.toString();
    super.okPressed();
  }
  
  @Override
  public void cancelPressed() {
    this.selectedImage = null;
    super.cancelPressed();
  }
  
  @Override
  public void configureShell(final Shell shell) {
    super.configureShell(shell);
    shell.setText("Docker Image Selection");
  }
  
  @Override
  public boolean isResizable() {
    return true;
  }
  
  @Override
  public Point getInitialSize() {
    return new Point(600, 700);
  }
  
  @Pure
  public String getSelectedImage() {
    return this.selectedImage;
  }
}
