package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.SWT
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Combo
import org.eclipse.jface.databinding.swt.typed.WidgetProperties
import org.eclipse.core.databinding.beans.typed.BeanProperties
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.conversion.IConverter
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog
import org.eclipse.swt.graphics.Point

abstract class AbstractJavaBaseGeneratorCompletionDialog
    extends AbstractCodeGeneratorCompletionDialog {
    var Argument generationSerializerArgument
    protected var Combo generationSerializer

    protected new(Shell parentShell, IProject project, IFile file) {
        super(parentShell, project, file, "Java Base Generator", "JBG")
    }

    final protected def setGenerationSerializerArgument(Argument generationSerializerArgument) {
        this.generationSerializerArgument = generationSerializerArgument
        // Let argument participate in configuration validation in
        // AbstractTemplateCompletionDialog.propertyChange()
        this.generationSerializerArgument.addPropertyChangeListener(this)
    }

    override createDialogArea(Composite parent) {
        val area = super.createDialogArea(parent)
        parent.shell.size = new Point(DEFAULT_WIDTH, 350)
        return area
    }

    override addAdditionalControlsToDialog(Composite parent) {
        parent.addGenerationSerializer
    }

    private def addGenerationSerializer(Composite parent) {
        new Label(parent, SWT.NULL).text = "Generation serializer:"
        generationSerializer = new Combo(parent, SWT.DROP_DOWN.bitwiseOr(SWT.READ_ONLY))
        val layoutData = new GridData(SWT.FILL, SWT.FILL, true, false)
        layoutData.horizontalSpan = 2
        generationSerializer.layoutData = layoutData

        val generatorParameters = JavaBaseGeneratorParameters.instance()
        generationSerializer.items = generatorParameters.serializerLabels
        val defaultItem = generatorParameters.defaultSerializerLabel
        generationSerializer.select(generationSerializer.items.indexOf(defaultItem))

        val target = WidgetProperties.comboSelection.observe(generationSerializer)
        val model = BeanProperties.value(Argument, "value").observe(generationSerializerArgument)

        val modelToTargetConverter = new UpdateValueStrategy<String, String>()
        modelToTargetConverter.converter = IConverter.create([
                generatorParameters.getSerializerLabel(it)
            ])

        val targetToModelConverter = new UpdateValueStrategy<String, String>()
        targetToModelConverter.converter = IConverter.create([
                generatorParameters.getSerializerParameterName(it)
            ])

        dataBindingContext.bindValue(target, model, targetToModelConverter, modelToTargetConverter)
    }

    final protected def printableModelFileType() {
        return switch(file.fileExtension) {
            case "services": "service"
            case "mapping": "mapping"
            default: throw new IllegalStateException("Unsupported model file extension " +
                '''".«file.fileExtension»"''')
        }
    }

    override close() {
        generationSerializerArgument.removePropertyChangeListener(this)
        super.close()
    }
}