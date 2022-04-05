package de.fhdo.lemma.model_processing.eclipse.launcher

import org.w3c.dom.Element
import java.util.List
import org.osgi.framework.FrameworkUtil
import java.util.Random
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.resources.IFile
import org.eclipse.swt.widgets.Text
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.swt.SWT
import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.fieldassist.ControlDecoration
import org.eclipse.jface.fieldassist.FieldDecorationRegistry
import java.nio.charset.StandardCharsets
import java.io.ByteArrayOutputStream
import org.eclipse.ui.console.MessageConsoleStream
import org.eclipse.ui.PlatformUI
import java.util.concurrent.TimeUnit
import de.fhdo.lemma.eclipse.ui.utils.LemmaUiUtils
import de.fhdo.lemma.data.DataModel
import de.fhdo.lemma.technology.Technology
import de.fhdo.lemma.service.ServiceModel
import de.fhdo.lemma.service.ImportType
import de.fhdo.lemma.operation.OperationModel
import de.fhdo.lemma.technology.mapping.TechnologyMapping
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.swt.widgets.Control
import org.eclipse.jface.databinding.swt.ISWTObservableValue
import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.emf.ecore.EObject
import de.fhdo.lemma.service.Import
import de.fhdo.lemma.utils.LemmaUtils
import java.io.ByteArrayInputStream
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import static de.fhdo.lemma.model_processing.eclipse.launcher.LaunchConfigurationConstants.*
import org.eclipse.jface.viewers.ISelection
import org.eclipse.core.runtime.Platform
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.part.FileEditorInput
import org.eclipse.ui.console.ConsolePlugin
import org.eclipse.ui.console.MessageConsole

final class Utils {
    static def findFileInWorkspaceProject(String projectName, String filename) {
        return try {
            findProjectInCurrentWorkspace(projectName)?.findMember(filename) as IFile
        } catch (ClassCastException ex) {
            null
        }
    }

    static def findProjectInCurrentWorkspace(String projectName) {
        return ResourcesPlugin.workspace.root.projects.findFirst[it.name == projectName]
    }

    static def preferenceQualifier() {
        FrameworkUtil.getBundle(Utils).symbolicName
    }

    static def notNullOrEmpty(String s, String errorMessage) {
        if (s.nullOrEmpty)
            throw new IllegalArgumentException(errorMessage)
    }

    static def notNull(Object o, String errorMessage) {
        if (o === null)
            throw new IllegalArgumentException(errorMessage)
    }

    static def notEmpty(String s, String errorMessage) {
        if (s.empty)
            throw new IllegalArgumentException(errorMessage)
    }

    static def findChildElementWithTag(Element parent, String tag) {
        val children = parent.childNodes
        for (n : 0..<children.length) {
            val child = children.item(n)
            if (child instanceof Element && (child as Element).tagName == tag)
                return child as Element
        }
        return null
    }

    static def <T> equalLists(List<T> l1, List<? extends T> l2) {
        if (l1.size !== l2.size)
            return false

        for (i : 0 ..< l1.size)
            if (l1.get(i) != l2.get(i))
                return false

        return true
    }

    static def messageOrTypeHint(Exception ex) {
        return if (!ex.message.nullOrEmpty)
            ex.message
        else
            '''Unknown (exception type: «ex.class.simpleName»)'''
    }

    static def <T> randomItemFrom(Iterable<T> iterable) {
        if (iterable.empty)
            throw new IllegalArgumentException("Iterable is empty and random item selection is " +
                "thus not possible")

        return iterable.toList.get(new Random().nextInt(iterable.size))
    }

    static def <T> flatCopy(List<T> list) {
        val copy = newArrayList()
        copy.addAll(list)
        return copy
    }

    static def <T> addInPlace(List<T> list, T element) {
        list.add(element)
        return list
    }

    static def <T> moveUpInPlace(List<T> list, T item) {
        val index = list.indexOf(item)
        switch (index) {
            case 0: {
                list.remove(0)
                list.add(item)
            }
            case index > 0: {
                list.add(index - 1, item)
                list.remove(index + 1)
            }
        }

        return list
    }

    static def <T> moveDownInPlace(List<T> list, T item) {
        val index = list.indexOf(item)
        val lastIndex = list.size - 1
        switch (index) {
            case index > -1 && index == lastIndex: {
                list.remove(lastIndex)
                list.add(0, item)
            }
            case index > -1: {
                list.remove(index)
                list.add(index + 1, item)
            }
        }

        return list
    }

    static def <T> removeInPlace(List<T> list, T element) {
        list.remove(element)
        return list
    }

    static def <T> removeAllInPlace(List<T> list, List<T> elements) {
        list.removeAll(elements)
        return list
    }

    static def <T> bindWithValidationDecorationSupport(
        Control control,
        DataBindingContext dataBindingContext,
        Class<T> beanClass,
        String propertyName,
        T source,
        (String)=>void validationProcedure
    ) {
        val target = control.toObservableValue
        val model = BeanProperties.value(beanClass, propertyName).observe(source)
        val updateStrategy = null

        val decoration = new ControlDecoration(control, SWT.TOP.bitwiseOr(SWT.LEFT))
        control.addValidationListener(decoration, validationProcedure)

        val bindValue = dataBindingContext.bindValue(target, model, updateStrategy, null)
        return bindValue -> decoration
    }

    private static dispatch def toObservableValue(Control control) {
        throw new IllegalArgumentException('''Creation of «ISWTObservableValue.simpleName» ''' +
            '''instance not supported for controls of type «control.class.simpleName»''')
    }

    private static dispatch def toObservableValue(Text text) {
        return WidgetProperties.text(SWT.Modify).observe(text)
    }

    private static dispatch def toObservableValue(Combo combo) {
        return WidgetProperties.comboSelection.observe(combo)
    }

    private static dispatch def addValidationListener(
        Control control,
        ControlDecoration decoration,
        (String)=>void validationProcedure
    ) {
        throw new IllegalArgumentException("Validation listeners not support for controls of " +
            '''type «control.class.simpleName»''')
    }

    private static dispatch def addValidationListener(Text text, ControlDecoration decoration,
        (String)=>void validationProcedure) {
        text.addModifyListener([
            try {
                validationProcedure.apply((widget as Text).text)
                decoration.hide()
            } catch (IllegalArgumentException ex) {
                decoration.descriptionText = ex.message
                decoration.image = FieldDecorationRegistry
                    .^default
                    .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                    .image
                decoration.show()
            }
        ])
    }

    private static dispatch def addValidationListener(Combo combo, ControlDecoration decoration,
        (String)=>void validationProcedure) {
        combo.addSelectionListener(new SelectionListener() {
            override widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event)
            }

            override widgetSelected(SelectionEvent event) {
                try {
                    validationProcedure.apply(combo.text)
                    decoration.hide()
                } catch (IllegalArgumentException ex) {
                    decoration.descriptionText = ex.message
                    decoration.image = FieldDecorationRegistry
                        .^default
                        .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                        .image
                    decoration.show()
                }
            }
        })
    }

//    static def <T> bindWithValidationDecorationSupport(
//        Text field,
//        DataBindingContext dataBindingContext,
//        Class<T> beanClass,
//        String propertyName,
//        T source, (String)=>void validationProcedure
//    ) {
//        val target = WidgetProperties.text(SWT.Modify).observe(field)
//        val model = BeanProperties.value(beanClass, propertyName).observe(source)
//        val updateStrategy = null /*new UpdateValueStrategy<String, String>()
//        updateStrategy.afterGetValidator = [value |
//            try {
//                validationProcedure.apply(value)
//                return ValidationStatus.ok()
//            } catch (IllegalArgumentException ex) {
//                return ValidationStatus.error(ex.message)
//            }
//        ]*/
//
//        //val decoration = ControlDecorationSupport.create(bindValue, SWT.TOP.bitwiseOr(SWT.LEFT))
//        val decoration = new ControlDecoration(field, SWT.TOP.bitwiseOr(SWT.LEFT))
//        field.addModifyListener([
//            try {
//                validationProcedure.apply((widget as Text).text)
//                decoration.hide()
//            } catch (IllegalArgumentException ex) {
//                decoration.descriptionText = ex.message
//                decoration.image = FieldDecorationRegistry
//                    .^default
//                    .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
//                    .image
//                decoration.show()
//            }
//        ])
//
//        val bindValue = dataBindingContext.bindValue(target, model, updateStrategy, null)
//        return bindValue -> decoration
//    }

    static def singleRowSelectionOrError(TableViewer viewer) {
        val selection = viewer.selection as IStructuredSelection
        return if (selection.empty) {
            MessageDialog.openError(viewer.control.shell, "No Row Selected", "Please select a " +
                "single row.")
            null
        } else if (selection.length > 1) {
            MessageDialog.openError(viewer.control.shell, "Too Many Rows Selected", "Please " +
                "select only a single row.")
            null
        } else
            selection
    }

    static def rowSelectionOrError(TableViewer viewer) {
        val selection = viewer.selection as IStructuredSelection
        return if (selection.empty) {
            MessageDialog.openError(viewer.control.shell, "No Rows Selected", "Please select at " +
                "least one row.")
            null
        } else
            selection
    }

    static def printlnSep(MessageConsoleStream stream, int length) {
        stream.println("-".repeat(length))
    }

    static def printlnIndent(MessageConsoleStream stream, String s) {
        stream.printlnIndent(s, 1)
    }

    public static val INDENT = " ".repeat(2)

    static def printlnIndent(MessageConsoleStream stream, String s, int numberOfIndents) {
        stream.println(INDENT.repeat(numberOfIndents) + s)
    }

    static def getActiveShell() {
        return PlatformUI.workbench.activeWorkbenchWindow.shell
    }

    static def getWorkbenchDisplay() {
        return PlatformUI.workbench.display
    }

    static def Pair<Integer, String> executeShellCommandBlocking(String command, int cycleTime,
        int maxCycles) {
        val process = Runtime.runtime.exec(command)
        val infoStream = new ByteArrayOutputStream()
        process.inputStream.transferTo(infoStream)
        val errorStream = new ByteArrayOutputStream()
        process.errorStream.transferTo(errorStream)

        var cycle = 0
        while(process.alive && cycle < maxCycles) {
            try {
                process.waitFor(cycleTime, TimeUnit.MILLISECONDS)
                cycle += 1
            } catch (InterruptedException ex) {
                process.destroyForcibly
                throw ex
            }
        }

        if (process.alive)
            throw new IllegalStateException('''Command «command» didn't finish in granted time ''' +
                '''(«cycleTime*maxCycles» ms)''')

        val messageStream = process.exitValue == 0 ? infoStream : errorStream
        return process.exitValue -> messageStream.toString(StandardCharsets.UTF_8)
    }

    static def parseImports(IFile file) {
        try {
            val resource = LemmaUiUtils.loadXtextResource(file)
            val modelRoot = resource.contents.get(0)
            return modelRoot.typedImports.toList as List<Pair<Class<? extends EObject>, ImportInfo>>
        } catch(Exception ex) {
            throw new IllegalArgumentException("Error during parsing of model file" +
                '''«file.name»: «ex.message»''')
        }
    }

    static dispatch def typedImports(DataModel modelRoot) {
        return modelRoot.complexTypeImports.map[DataModel -> new ImportInfo(it.name, it.importURI)]
    }

    static class ImportInfo {
        @Accessors(PUBLIC_GETTER)
        val String alias
        @Accessors(PUBLIC_GETTER)
        val String importUri
        new(String alias, String importUri) {
            this.alias = alias
            this.importUri = importUri
        }
    }

    static dispatch def typedImports(Technology modelRoot) {
        return modelRoot.imports.map[Technology -> new ImportInfo(it.name, it.importURI)]
    }

    static dispatch def typedImports(ServiceModel modelRoot) {
        return modelRoot.imports.map[
            it.importType.modelRootClass -> new ImportInfo(it.name, it.importURI)
        ]
    }

    static dispatch def typedImports(TechnologyMapping modelRoot) {
        return modelRoot.imports.map[
            it.importType.modelRootClass -> new ImportInfo(it.name, it.importURI)
        ]
    }

    static dispatch def typedImports(OperationModel modelRoot) {
        return modelRoot.imports.map[
            it.importType.modelRootClass -> new ImportInfo(it.name, it.importURI)
        ]
    }

    private static def getModelRootClass(ImportType importType) {
        return switch(importType) {
            case DATATYPES: DataModel
            case TECHNOLOGY: Technology
            case MICROSERVICES: ServiceModel
            case OPERATION_NODES: OperationModel
        }
    }

    static dispatch def makeImportPathsAbsolute(DataModel modelRoot, IFile basefile) {
        modelRoot.makeImportPathsAbsoluteFromBasefilePath(
            basefile.rawLocation.makeAbsolute.toString
        )
    }

    static dispatch def makeImportPathsAbsoluteFromBasefilePath(DataModel modelRoot,
        String absoluteBasefilePath) {
        modelRoot.complexTypeImports.forEach[
            it.importURI = LemmaUtils.convertToAbsolutePath(it.importURI, absoluteBasefilePath)
        ]
    }

    static dispatch def makeImportPathsAbsolute(Technology modelRoot, IFile basefile) {
        modelRoot.makeImportPathsAbsoluteFromBasefilePath(
            basefile.rawLocation.makeAbsolute.toString
        )
    }

    static dispatch def makeImportPathsAbsoluteFromBasefilePath(Technology modelRoot,
        String absoluteBasefilePath) {
        modelRoot.imports.forEach[
            it.importURI = LemmaUtils.convertToAbsolutePath(it.importURI, absoluteBasefilePath)
        ]
    }

    static dispatch def makeImportPathsAbsolute(ServiceModel modelRoot, IFile basefile) {
        modelRoot.makeImportPathsAbsoluteFromBasefilePath(
            basefile.rawLocation.makeAbsolute.toString
        )
    }

    static dispatch def makeImportPathsAbsoluteFromBasefilePath(ServiceModel modelRoot,
        String absoluteBasefilePath) {
        modelRoot.imports.forEach[it.makeImportPathAbsolute(absoluteBasefilePath)]
    }

    private static def makeImportPathAbsolute(Import ^import, String absoluteBasefilePath) {
        import.importURI = LemmaUtils.convertToAbsolutePath(import.importURI,
            absoluteBasefilePath)
    }

    static dispatch def makeImportPathsAbsolute(TechnologyMapping modelRoot, IFile basefile) {
        modelRoot.makeImportPathsAbsoluteFromBasefilePath(
            basefile.rawLocation.makeAbsolute.toString
        )
    }

    static dispatch def makeImportPathsAbsoluteFromBasefilePath(TechnologyMapping modelRoot,
        String absoluteBasefilePath) {
        modelRoot.imports.forEach[it.makeImportPathAbsolute(absoluteBasefilePath)]
    }

    static dispatch def makeImportPathsAbsolute(OperationModel modelRoot, IFile basefile) {
        modelRoot.makeImportPathsAbsoluteFromBasefilePath(
            basefile.rawLocation.makeAbsolute.toString
        )
    }

    static dispatch def makeImportPathsAbsoluteFromBasefilePath(OperationModel modelRoot,
        String absoluteBasefilePath) {
        modelRoot.imports.forEach[it.makeImportPathAbsolute(absoluteBasefilePath)]
    }

    // Dirty hack to programmatically trigger the validation of a bound text field.
    static def triggerValidation(Text field) {
        val currentText = field.text
        // Programmatically trigger text field validation by changing its text to a
        // different value than the current one
        field.text = '''«currentText»TriggerUpdate'''
        field.text = currentText
    }

    static def parseXmlString(String xml) {
        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return builder.parse(new ByteArrayInputStream(xml.bytes))
    }

    static def getRootElementWithTag(Document document, String tag) {
        val rootElement = document.documentElement
        return if (rootElement !== null && rootElement instanceof Element &&
            (rootElement as Element).tagName == tag)
            rootElement as Element
        else
            null
    }

    static def getProcessingConfigurationLaunchConfigurations() {
        return LAUNCH_MANAGER
            .getLaunchConfigurations(PROCESSING_CONFIGURATION_LAUNCH_CONFIGURATION_TYPE)
            .toMap([it.name], [it])
    }

    static def getSelectedFile(ISelection selection) {
        return if (selection instanceof IStructuredSelection) {
            val structuredSelection = selection as IStructuredSelection
            Platform.adapterManager.getAdapter(structuredSelection.firstElement, IFile)
        } else
            null
    }

    static def getEditedFile(IEditorPart editor) {
        return if (editor.editorInput instanceof FileEditorInput) {
               val path = (editor.editorInput as FileEditorInput).path
                ResourcesPlugin.workspace.root.getFileForLocation(path)
            } else
                null
    }

    static def getAndRevealConsole(String name) {
        val consoleManager = ConsolePlugin.^default.consoleManager
        var console = consoleManager.consoles.findFirst[it.name == name]
        if (console === null) {
            console = new MessageConsole(name, null)
            consoleManager.addConsoles(#[console])
        }

        consoleManager.showConsoleView(console)

        return console as MessageConsole
    }

    static def newErrorMessageStream(MessageConsole console) {
        val stream = console.newMessageStream
        // Color setting needs to be done by UI thread because it enforces redrawing the widget and
        // this otherwise would result in an invalid thread access
        getWorkbenchDisplay.syncExec([stream.color = CONSOLE_ERROR_COLOR])
        return stream
    }
}