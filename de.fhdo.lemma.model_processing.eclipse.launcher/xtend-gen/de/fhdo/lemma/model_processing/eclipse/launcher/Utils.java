package de.fhdo.lemma.model_processing.eclipse.launcher;

import com.google.common.base.Objects;
import de.fhdo.lemma.data.ComplexTypeImport;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils;
import de.fhdo.lemma.operation.OperationModel;
import de.fhdo.lemma.service.Import;
import de.fhdo.lemma.service.ImportType;
import de.fhdo.lemma.service.ServiceModel;
import de.fhdo.lemma.technology.Technology;
import de.fhdo.lemma.technology.TechnologyImport;
import de.fhdo.lemma.technology.mapping.TechnologyMapping;
import de.fhdo.lemma.utils.LemmaUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.osgi.framework.FrameworkUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("all")
public final class Utils {
  public static class ImportInfo {
    @Accessors(AccessorType.PUBLIC_GETTER)
    private final String alias;
    
    @Accessors(AccessorType.PUBLIC_GETTER)
    private final String importUri;
    
    public ImportInfo(final String alias, final String importUri) {
      this.alias = alias;
      this.importUri = importUri;
    }
    
    @Pure
    public String getAlias() {
      return this.alias;
    }
    
    @Pure
    public String getImportUri() {
      return this.importUri;
    }
  }
  
  public static IFile findFileInWorkspaceProject(final String projectName, final String filename) {
    IFile _xtrycatchfinallyexpression = null;
    try {
      IProject _findProjectInCurrentWorkspace = Utils.findProjectInCurrentWorkspace(projectName);
      IResource _findMember = null;
      if (_findProjectInCurrentWorkspace!=null) {
        _findMember=_findProjectInCurrentWorkspace.findMember(filename);
      }
      _xtrycatchfinallyexpression = ((IFile) _findMember);
    } catch (final Throwable _t) {
      if (_t instanceof ClassCastException) {
        _xtrycatchfinallyexpression = null;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return _xtrycatchfinallyexpression;
  }
  
  public static IProject findProjectInCurrentWorkspace(final String projectName) {
    final Function1<IProject, Boolean> _function = (IProject it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, projectName));
    };
    return IterableExtensions.<IProject>findFirst(((Iterable<IProject>)Conversions.doWrapArray(ResourcesPlugin.getWorkspace().getRoot().getProjects())), _function);
  }
  
  public static String preferenceQualifier() {
    return FrameworkUtil.getBundle(Utils.class).getSymbolicName();
  }
  
  public static void notNullOrEmpty(final String s, final String errorMessage) {
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(s);
    if (_isNullOrEmpty) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
  
  public static void notNull(final Object o, final String errorMessage) {
    if ((o == null)) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
  
  public static void notEmpty(final String s, final String errorMessage) {
    boolean _isEmpty = s.isEmpty();
    if (_isEmpty) {
      throw new IllegalArgumentException(errorMessage);
    }
  }
  
  public static Element findChildElementWithTag(final Element parent, final String tag) {
    final NodeList children = parent.getChildNodes();
    int _length = children.getLength();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer n : _doubleDotLessThan) {
      {
        final Node child = children.item((n).intValue());
        if (((child instanceof Element) && Objects.equal(((Element) child).getTagName(), tag))) {
          return ((Element) child);
        }
      }
    }
    return null;
  }
  
  public static <T extends Object> boolean equalLists(final List<T> l1, final List<? extends T> l2) {
    int _size = l1.size();
    int _size_1 = l2.size();
    boolean _tripleNotEquals = (_size != _size_1);
    if (_tripleNotEquals) {
      return false;
    }
    int _size_2 = l1.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_2, true);
    for (final Integer i : _doubleDotLessThan) {
      T _get = l1.get((i).intValue());
      T _get_1 = l2.get((i).intValue());
      boolean _notEquals = (!Objects.equal(_get, _get_1));
      if (_notEquals) {
        return false;
      }
    }
    return true;
  }
  
  public static String messageOrTypeHint(final Exception ex) {
    String _xifexpression = null;
    boolean _isNullOrEmpty = StringExtensions.isNullOrEmpty(ex.getMessage());
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      _xifexpression = ex.getMessage();
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("Unknown (exception type: ");
      String _simpleName = ex.getClass().getSimpleName();
      _builder.append(_simpleName);
      _builder.append(")");
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  public static <T extends Object> T randomItemFrom(final Iterable<T> iterable) {
    boolean _isEmpty = IterableExtensions.isEmpty(iterable);
    if (_isEmpty) {
      throw new IllegalArgumentException(("Iterable is empty and random item selection is " + 
        "thus not possible"));
    }
    return IterableExtensions.<T>toList(iterable).get(new Random().nextInt(IterableExtensions.size(iterable)));
  }
  
  public static <T extends Object> ArrayList<T> flatCopy(final List<T> list) {
    final ArrayList<T> copy = CollectionLiterals.<T>newArrayList();
    copy.addAll(list);
    return copy;
  }
  
  public static <T extends Object> List<T> addInPlace(final List<T> list, final T element) {
    list.add(element);
    return list;
  }
  
  public static <T extends Object> List<T> moveUpInPlace(final List<T> list, final T item) {
    final int index = list.indexOf(item);
    boolean _matched = false;
    if (Objects.equal(index, 0)) {
      _matched=true;
      list.remove(0);
      list.add(item);
    }
    if (!_matched) {
      if ((index > 0)) {
        _matched=true;
        list.add((index - 1), item);
        list.remove((index + 1));
      }
    }
    return list;
  }
  
  public static <T extends Object> List<T> moveDownInPlace(final List<T> list, final T item) {
    final int index = list.indexOf(item);
    int _size = list.size();
    final int lastIndex = (_size - 1);
    boolean _matched = false;
    if (((index > (-1)) && (index == lastIndex))) {
      _matched=true;
      list.remove(lastIndex);
      list.add(0, item);
    }
    if (!_matched) {
      if ((index > (-1))) {
        _matched=true;
        list.remove(index);
        list.add((index + 1), item);
      }
    }
    return list;
  }
  
  public static <T extends Object> List<T> removeInPlace(final List<T> list, final T element) {
    list.remove(element);
    return list;
  }
  
  public static <T extends Object> List<T> removeAllInPlace(final List<T> list, final List<T> elements) {
    list.removeAll(elements);
    return list;
  }
  
  public static <T extends Object> Pair<Binding, ControlDecoration> bindWithValidationDecorationSupport(final Control control, final DataBindingContext dataBindingContext, final Class<T> beanClass, final String propertyName, final T source, final Procedure1<? super String> validationProcedure) {
    final ISWTObservableValue<String> target = Utils.toObservableValue(control);
    final IObservableValue<Object> model = BeanProperties.<T, Object>value(beanClass, propertyName).observe(source);
    final Object updateStrategy = null;
    final ControlDecoration decoration = new ControlDecoration(control, (SWT.TOP | SWT.LEFT));
    Utils.addValidationListener(control, decoration, validationProcedure);
    final Binding bindValue = dataBindingContext.<String, Object>bindValue(target, model, ((UpdateValueStrategy<? super String, ?>)updateStrategy), null);
    return Pair.<Binding, ControlDecoration>of(bindValue, decoration);
  }
  
  private static ISWTObservableValue<String> _toObservableValue(final Control control) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Creation of ");
    String _simpleName = ISWTObservableValue.class.getSimpleName();
    _builder.append(_simpleName);
    _builder.append(" ");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("instance not supported for controls of type ");
    String _simpleName_1 = control.getClass().getSimpleName();
    _builder_1.append(_simpleName_1);
    String _plus = (_builder.toString() + _builder_1);
    throw new IllegalArgumentException(_plus);
  }
  
  private static ISWTObservableValue<String> _toObservableValue(final Text text) {
    return WidgetProperties.<Text>text(SWT.Modify).observe(text);
  }
  
  private static ISWTObservableValue<String> _toObservableValue(final Combo combo) {
    return WidgetProperties.comboSelection().observe(combo);
  }
  
  private static void _addValidationListener(final Control control, final ControlDecoration decoration, final Procedure1<? super String> validationProcedure) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type ");
    String _simpleName = control.getClass().getSimpleName();
    _builder.append(_simpleName);
    String _plus = ("Validation listeners not support for controls of " + _builder);
    throw new IllegalArgumentException(_plus);
  }
  
  private static void _addValidationListener(final Text text, final ControlDecoration decoration, final Procedure1<? super String> validationProcedure) {
    final ModifyListener _function = (ModifyEvent it) -> {
      try {
        validationProcedure.apply(((Text) it.widget).getText());
        decoration.hide();
      } catch (final Throwable _t) {
        if (_t instanceof IllegalArgumentException) {
          final IllegalArgumentException ex = (IllegalArgumentException)_t;
          decoration.setDescriptionText(ex.getMessage());
          decoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
          decoration.show();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    };
    text.addModifyListener(_function);
  }
  
  private static void _addValidationListener(final Combo combo, final ControlDecoration decoration, final Procedure1<? super String> validationProcedure) {
    combo.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetDefaultSelected(final SelectionEvent event) {
        this.widgetSelected(event);
      }
      
      @Override
      public void widgetSelected(final SelectionEvent event) {
        try {
          validationProcedure.apply(combo.getText());
          decoration.hide();
        } catch (final Throwable _t) {
          if (_t instanceof IllegalArgumentException) {
            final IllegalArgumentException ex = (IllegalArgumentException)_t;
            decoration.setDescriptionText(ex.getMessage());
            decoration.setImage(FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage());
            decoration.show();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    });
  }
  
  public static IStructuredSelection singleRowSelectionOrError(final TableViewer viewer) {
    ISelection _selection = viewer.getSelection();
    final IStructuredSelection selection = ((IStructuredSelection) _selection);
    IStructuredSelection _xifexpression = null;
    boolean _isEmpty = selection.isEmpty();
    if (_isEmpty) {
      Object _xblockexpression = null;
      {
        MessageDialog.openError(viewer.getControl().getShell(), "No Row Selected", ("Please select a " + 
          "single row."));
        _xblockexpression = null;
      }
      _xifexpression = ((IStructuredSelection)_xblockexpression);
    } else {
      IStructuredSelection _xifexpression_1 = null;
      int _length = ((Object[])Conversions.unwrapArray(selection, Object.class)).length;
      boolean _greaterThan = (_length > 1);
      if (_greaterThan) {
        Object _xblockexpression_1 = null;
        {
          MessageDialog.openError(viewer.getControl().getShell(), "Too Many Rows Selected", ("Please " + 
            "select only a single row."));
          _xblockexpression_1 = null;
        }
        _xifexpression_1 = ((IStructuredSelection)_xblockexpression_1);
      } else {
        _xifexpression_1 = selection;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public static IStructuredSelection rowSelectionOrError(final TableViewer viewer) {
    ISelection _selection = viewer.getSelection();
    final IStructuredSelection selection = ((IStructuredSelection) _selection);
    IStructuredSelection _xifexpression = null;
    boolean _isEmpty = selection.isEmpty();
    if (_isEmpty) {
      Object _xblockexpression = null;
      {
        MessageDialog.openError(viewer.getControl().getShell(), "No Rows Selected", ("Please select at " + 
          "least one row."));
        _xblockexpression = null;
      }
      _xifexpression = ((IStructuredSelection)_xblockexpression);
    } else {
      _xifexpression = selection;
    }
    return _xifexpression;
  }
  
  public static void printlnSep(final MessageConsoleStream stream, final int length) {
    stream.println("-".repeat(length));
  }
  
  public static void printlnIndent(final MessageConsoleStream stream, final String s) {
    Utils.printlnIndent(stream, s, 1);
  }
  
  public static final String INDENT = " ".repeat(2);
  
  public static void printlnIndent(final MessageConsoleStream stream, final String s, final int numberOfIndents) {
    String _repeat = Utils.INDENT.repeat(numberOfIndents);
    String _plus = (_repeat + s);
    stream.println(_plus);
  }
  
  public static Shell getActiveShell() {
    return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
  }
  
  public static Display getWorkbenchDisplay() {
    return PlatformUI.getWorkbench().getDisplay();
  }
  
  public static Pair<Integer, String> executeShellCommandBlocking(final String command, final int cycleTime, final int maxCycles) {
    try {
      final Process process = Runtime.getRuntime().exec(command);
      final ByteArrayOutputStream infoStream = new ByteArrayOutputStream();
      process.getInputStream().transferTo(infoStream);
      final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
      process.getErrorStream().transferTo(errorStream);
      int cycle = 0;
      while ((process.isAlive() && (cycle < maxCycles))) {
        try {
          process.waitFor(cycleTime, TimeUnit.MILLISECONDS);
          int _cycle = cycle;
          cycle = (_cycle + 1);
        } catch (final Throwable _t) {
          if (_t instanceof InterruptedException) {
            final InterruptedException ex = (InterruptedException)_t;
            process.destroyForcibly();
            throw ex;
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
      boolean _isAlive = process.isAlive();
      if (_isAlive) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("Command ");
        _builder.append(command);
        _builder.append(" didn\'t finish in granted time ");
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("(");
        _builder_1.append((cycleTime * maxCycles));
        _builder_1.append(" ms)");
        String _plus = (_builder.toString() + _builder_1);
        throw new IllegalStateException(_plus);
      }
      ByteArrayOutputStream _xifexpression = null;
      int _exitValue = process.exitValue();
      boolean _equals = (_exitValue == 0);
      if (_equals) {
        _xifexpression = infoStream;
      } else {
        _xifexpression = errorStream;
      }
      final ByteArrayOutputStream messageStream = _xifexpression;
      int _exitValue_1 = process.exitValue();
      String _string = messageStream.toString(StandardCharsets.UTF_8);
      return Pair.<Integer, String>of(Integer.valueOf(_exitValue_1), _string);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static List<Pair<Class<? extends EObject>, Utils.ImportInfo>> parseImports(final IFile file) {
    try {
      final XtextResource resource = LemmaUiUtils.loadXtextResource(file);
      final EObject modelRoot = resource.getContents().get(0);
      List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _list = IterableExtensions.toList(Utils.typedImports(modelRoot));
      return ((List<Pair<Class<? extends EObject>, Utils.ImportInfo>>) _list);
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception ex = (Exception)_t;
        StringConcatenation _builder = new StringConcatenation();
        String _name = file.getName();
        _builder.append(_name);
        _builder.append(": ");
        String _message = ex.getMessage();
        _builder.append(_message);
        String _plus = ("Error during parsing of model file" + _builder);
        throw new IllegalArgumentException(_plus);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  protected static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _typedImports(final DataModel modelRoot) {
    final Function1<ComplexTypeImport, Pair<Class<DataModel>, Utils.ImportInfo>> _function = (ComplexTypeImport it) -> {
      String _name = it.getName();
      String _importURI = it.getImportURI();
      Utils.ImportInfo _importInfo = new Utils.ImportInfo(_name, _importURI);
      return Pair.<Class<DataModel>, Utils.ImportInfo>of(DataModel.class, _importInfo);
    };
    return ListExtensions.<ComplexTypeImport, Pair<Class<DataModel>, Utils.ImportInfo>>map(modelRoot.getComplexTypeImports(), _function);
  }
  
  protected static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _typedImports(final Technology modelRoot) {
    final Function1<TechnologyImport, Pair<Class<Technology>, Utils.ImportInfo>> _function = (TechnologyImport it) -> {
      String _name = it.getName();
      String _importURI = it.getImportURI();
      Utils.ImportInfo _importInfo = new Utils.ImportInfo(_name, _importURI);
      return Pair.<Class<Technology>, Utils.ImportInfo>of(Technology.class, _importInfo);
    };
    return ListExtensions.<TechnologyImport, Pair<Class<Technology>, Utils.ImportInfo>>map(modelRoot.getImports(), _function);
  }
  
  protected static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _typedImports(final ServiceModel modelRoot) {
    final Function1<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>> _function = (Import it) -> {
      Class<? extends EObject> _modelRootClass = Utils.getModelRootClass(it.getImportType());
      String _name = it.getName();
      String _importURI = it.getImportURI();
      Utils.ImportInfo _importInfo = new Utils.ImportInfo(_name, _importURI);
      return Pair.<Class<? extends EObject>, Utils.ImportInfo>of(_modelRootClass, _importInfo);
    };
    return ListExtensions.<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>>map(modelRoot.getImports(), _function);
  }
  
  protected static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _typedImports(final TechnologyMapping modelRoot) {
    final Function1<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>> _function = (Import it) -> {
      Class<? extends EObject> _modelRootClass = Utils.getModelRootClass(it.getImportType());
      String _name = it.getName();
      String _importURI = it.getImportURI();
      Utils.ImportInfo _importInfo = new Utils.ImportInfo(_name, _importURI);
      return Pair.<Class<? extends EObject>, Utils.ImportInfo>of(_modelRootClass, _importInfo);
    };
    return ListExtensions.<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>>map(modelRoot.getImports(), _function);
  }
  
  protected static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> _typedImports(final OperationModel modelRoot) {
    final Function1<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>> _function = (Import it) -> {
      Class<? extends EObject> _modelRootClass = Utils.getModelRootClass(it.getImportType());
      String _name = it.getName();
      String _importURI = it.getImportURI();
      Utils.ImportInfo _importInfo = new Utils.ImportInfo(_name, _importURI);
      return Pair.<Class<? extends EObject>, Utils.ImportInfo>of(_modelRootClass, _importInfo);
    };
    return ListExtensions.<Import, Pair<Class<? extends EObject>, Utils.ImportInfo>>map(modelRoot.getImports(), _function);
  }
  
  private static Class<? extends EObject> getModelRootClass(final ImportType importType) {
    Class<? extends EObject> _switchResult = null;
    if (importType != null) {
      switch (importType) {
        case DATATYPES:
          _switchResult = DataModel.class;
          break;
        case TECHNOLOGY:
          _switchResult = Technology.class;
          break;
        case MICROSERVICES:
          _switchResult = ServiceModel.class;
          break;
        case OPERATION_NODES:
          _switchResult = OperationModel.class;
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }
  
  protected static void _makeImportPathsAbsolute(final DataModel modelRoot, final IFile basefile) {
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, 
      basefile.getRawLocation().makeAbsolute().toString());
  }
  
  protected static void _makeImportPathsAbsoluteFromBasefilePath(final DataModel modelRoot, final String absoluteBasefilePath) {
    final Consumer<ComplexTypeImport> _function = (ComplexTypeImport it) -> {
      it.setImportURI(LemmaUtils.convertToAbsolutePath(it.getImportURI(), absoluteBasefilePath));
    };
    modelRoot.getComplexTypeImports().forEach(_function);
  }
  
  protected static void _makeImportPathsAbsolute(final Technology modelRoot, final IFile basefile) {
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, 
      basefile.getRawLocation().makeAbsolute().toString());
  }
  
  protected static void _makeImportPathsAbsoluteFromBasefilePath(final Technology modelRoot, final String absoluteBasefilePath) {
    final Consumer<TechnologyImport> _function = (TechnologyImport it) -> {
      it.setImportURI(LemmaUtils.convertToAbsolutePath(it.getImportURI(), absoluteBasefilePath));
    };
    modelRoot.getImports().forEach(_function);
  }
  
  protected static void _makeImportPathsAbsolute(final ServiceModel modelRoot, final IFile basefile) {
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, 
      basefile.getRawLocation().makeAbsolute().toString());
  }
  
  protected static void _makeImportPathsAbsoluteFromBasefilePath(final ServiceModel modelRoot, final String absoluteBasefilePath) {
    final Consumer<Import> _function = (Import it) -> {
      Utils.makeImportPathAbsolute(it, absoluteBasefilePath);
    };
    modelRoot.getImports().forEach(_function);
  }
  
  private static void makeImportPathAbsolute(final Import import_, final String absoluteBasefilePath) {
    import_.setImportURI(LemmaUtils.convertToAbsolutePath(import_.getImportURI(), absoluteBasefilePath));
  }
  
  protected static void _makeImportPathsAbsolute(final TechnologyMapping modelRoot, final IFile basefile) {
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, 
      basefile.getRawLocation().makeAbsolute().toString());
  }
  
  protected static void _makeImportPathsAbsoluteFromBasefilePath(final TechnologyMapping modelRoot, final String absoluteBasefilePath) {
    final Consumer<Import> _function = (Import it) -> {
      Utils.makeImportPathAbsolute(it, absoluteBasefilePath);
    };
    modelRoot.getImports().forEach(_function);
  }
  
  protected static void _makeImportPathsAbsolute(final OperationModel modelRoot, final IFile basefile) {
    Utils.makeImportPathsAbsoluteFromBasefilePath(modelRoot, 
      basefile.getRawLocation().makeAbsolute().toString());
  }
  
  protected static void _makeImportPathsAbsoluteFromBasefilePath(final OperationModel modelRoot, final String absoluteBasefilePath) {
    final Consumer<Import> _function = (Import it) -> {
      Utils.makeImportPathAbsolute(it, absoluteBasefilePath);
    };
    modelRoot.getImports().forEach(_function);
  }
  
  public static void triggerValidation(final Text field) {
    final String currentText = field.getText();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(currentText);
    _builder.append("TriggerUpdate");
    field.setText(_builder.toString());
    field.setText(currentText);
  }
  
  public static Document parseXmlString(final String xml) {
    try {
      final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      byte[] _bytes = xml.getBytes();
      ByteArrayInputStream _byteArrayInputStream = new ByteArrayInputStream(_bytes);
      return builder.parse(_byteArrayInputStream);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static Element getRootElementWithTag(final Document document, final String tag) {
    final Element rootElement = document.getDocumentElement();
    Element _xifexpression = null;
    if ((((rootElement != null) && (rootElement instanceof Element)) && 
      Objects.equal(((Element) rootElement).getTagName(), tag))) {
      _xifexpression = ((Element) rootElement);
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static Map<String, ILaunchConfiguration> getProcessingConfigurationLaunchConfigurations() {
    try {
      final Function1<ILaunchConfiguration, String> _function = (ILaunchConfiguration it) -> {
        return it.getName();
      };
      final Function1<ILaunchConfiguration, ILaunchConfiguration> _function_1 = (ILaunchConfiguration it) -> {
        return it;
      };
      return IterableExtensions.<ILaunchConfiguration, String, ILaunchConfiguration>toMap(((Iterable<? extends ILaunchConfiguration>)Conversions.doWrapArray(LaunchConfigurationConstants.LAUNCH_MANAGER.getLaunchConfigurations(LaunchConfigurationConstants.PROCESSING_CONFIGURATION_LAUNCH_CONFIGURATION_TYPE))), _function, _function_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static IFile getSelectedFile(final ISelection selection) {
    IFile _xifexpression = null;
    if ((selection instanceof IStructuredSelection)) {
      IFile _xblockexpression = null;
      {
        final IStructuredSelection structuredSelection = ((IStructuredSelection) selection);
        _xblockexpression = Platform.getAdapterManager().<IFile>getAdapter(structuredSelection.getFirstElement(), IFile.class);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static IFile getEditedFile(final IEditorPart editor) {
    IFile _xifexpression = null;
    IEditorInput _editorInput = editor.getEditorInput();
    if ((_editorInput instanceof FileEditorInput)) {
      IFile _xblockexpression = null;
      {
        IEditorInput _editorInput_1 = editor.getEditorInput();
        final IPath path = ((FileEditorInput) _editorInput_1).getPath();
        _xblockexpression = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  public static MessageConsole getAndRevealConsole(final String name) {
    final IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
    final Function1<IConsole, Boolean> _function = (IConsole it) -> {
      String _name = it.getName();
      return Boolean.valueOf(Objects.equal(_name, name));
    };
    IConsole console = IterableExtensions.<IConsole>findFirst(((Iterable<IConsole>)Conversions.doWrapArray(consoleManager.getConsoles())), _function);
    if ((console == null)) {
      MessageConsole _messageConsole = new MessageConsole(name, null);
      console = _messageConsole;
      consoleManager.addConsoles(new IConsole[] { console });
    }
    consoleManager.showConsoleView(console);
    return ((MessageConsole) console);
  }
  
  public static MessageConsoleStream newErrorMessageStream(final MessageConsole console) {
    final MessageConsoleStream stream = console.newMessageStream();
    final Runnable _function = () -> {
      stream.setColor(LaunchConfigurationConstants.CONSOLE_ERROR_COLOR);
    };
    Utils.getWorkbenchDisplay().syncExec(_function);
    return stream;
  }
  
  private static ISWTObservableValue<String> toObservableValue(final Control combo) {
    if (combo instanceof Combo) {
      return _toObservableValue((Combo)combo);
    } else if (combo instanceof Text) {
      return _toObservableValue((Text)combo);
    } else if (combo != null) {
      return _toObservableValue(combo);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(combo).toString());
    }
  }
  
  private static void addValidationListener(final Control combo, final ControlDecoration decoration, final Procedure1<? super String> validationProcedure) {
    if (combo instanceof Combo
         && validationProcedure != null) {
      _addValidationListener((Combo)combo, decoration, validationProcedure);
      return;
    } else if (combo instanceof Text
         && validationProcedure != null) {
      _addValidationListener((Text)combo, decoration, validationProcedure);
      return;
    } else if (combo != null
         && validationProcedure != null) {
      _addValidationListener(combo, decoration, validationProcedure);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(combo, decoration, validationProcedure).toString());
    }
  }
  
  public static List<? extends Pair<? extends Class<? extends EObject>, Utils.ImportInfo>> typedImports(final EObject modelRoot) {
    if (modelRoot instanceof DataModel) {
      return _typedImports((DataModel)modelRoot);
    } else if (modelRoot instanceof OperationModel) {
      return _typedImports((OperationModel)modelRoot);
    } else if (modelRoot instanceof ServiceModel) {
      return _typedImports((ServiceModel)modelRoot);
    } else if (modelRoot instanceof Technology) {
      return _typedImports((Technology)modelRoot);
    } else if (modelRoot instanceof TechnologyMapping) {
      return _typedImports((TechnologyMapping)modelRoot);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(modelRoot).toString());
    }
  }
  
  public static void makeImportPathsAbsolute(final EObject modelRoot, final IFile basefile) {
    if (modelRoot instanceof DataModel) {
      _makeImportPathsAbsolute((DataModel)modelRoot, basefile);
      return;
    } else if (modelRoot instanceof OperationModel) {
      _makeImportPathsAbsolute((OperationModel)modelRoot, basefile);
      return;
    } else if (modelRoot instanceof ServiceModel) {
      _makeImportPathsAbsolute((ServiceModel)modelRoot, basefile);
      return;
    } else if (modelRoot instanceof Technology) {
      _makeImportPathsAbsolute((Technology)modelRoot, basefile);
      return;
    } else if (modelRoot instanceof TechnologyMapping) {
      _makeImportPathsAbsolute((TechnologyMapping)modelRoot, basefile);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(modelRoot, basefile).toString());
    }
  }
  
  public static void makeImportPathsAbsoluteFromBasefilePath(final EObject modelRoot, final String absoluteBasefilePath) {
    if (modelRoot instanceof DataModel) {
      _makeImportPathsAbsoluteFromBasefilePath((DataModel)modelRoot, absoluteBasefilePath);
      return;
    } else if (modelRoot instanceof OperationModel) {
      _makeImportPathsAbsoluteFromBasefilePath((OperationModel)modelRoot, absoluteBasefilePath);
      return;
    } else if (modelRoot instanceof ServiceModel) {
      _makeImportPathsAbsoluteFromBasefilePath((ServiceModel)modelRoot, absoluteBasefilePath);
      return;
    } else if (modelRoot instanceof Technology) {
      _makeImportPathsAbsoluteFromBasefilePath((Technology)modelRoot, absoluteBasefilePath);
      return;
    } else if (modelRoot instanceof TechnologyMapping) {
      _makeImportPathsAbsoluteFromBasefilePath((TechnologyMapping)modelRoot, absoluteBasefilePath);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(modelRoot, absoluteBasefilePath).toString());
    }
  }
}
