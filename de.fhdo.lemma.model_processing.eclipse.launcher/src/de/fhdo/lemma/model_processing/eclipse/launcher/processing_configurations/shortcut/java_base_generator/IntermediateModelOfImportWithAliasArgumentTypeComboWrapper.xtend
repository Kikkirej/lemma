package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.java_base_generator

import org.eclipse.swt.widgets.Combo
import org.eclipse.swt.widgets.Composite
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.Argument
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import org.eclipse.xtend.lib.annotations.Accessors
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.arguments.types.IntermediateModelOfImportWithAliasArgumentType
import org.eclipse.emf.ecore.EObject
import java.util.List
import org.eclipse.jface.fieldassist.ControlDecoration
import org.eclipse.swt.SWT
import org.eclipse.jface.fieldassist.FieldDecorationRegistry

class IntermediateModelOfImportWithAliasArgumentTypeComboWrapper {
    val ProcessingConfiguration processingConfiguration
    @Accessors(PUBLIC_GETTER)
    val Argument argument
    // We don't subclass Combo since it is effectively prohibited (cf. Combo.checkSubclass()). While
    // we could override checkSubclass() to enable subclassing, we don't do it to be compatible with
    // future SWT releases (cf. the Javadoc of Widget.checkSubclass())
    @Accessors(PUBLIC_GETTER)
    val Combo combo

    new(Composite parent, int style, ProcessingConfiguration processingConfiguration,
        String parameterName) {
        this(parent, style, processingConfiguration, parameterName, #[])
    }

    new(Composite parent, int style, ProcessingConfiguration processingConfiguration,
        String parameterName, List<Class<? extends EObject>> filteredModelTypes) {
        combo = new Combo(parent, style)
        this.processingConfiguration = processingConfiguration
        this.argument = Argument.newArgument
            .singleValued
            .intermediateModelOfImportWithAlias("")
            .parameter(parameterName)
        addItems(filteredModelTypes)
    }

    private def addItems(List<Class<? extends EObject>> filteredModelTypes) {
        try {
            val importAliases = (argument.type as IntermediateModelOfImportWithAliasArgumentType)
                .parseTransformableImportedModelsOfSourceModelFile(processingConfiguration)
                .filter[filteredModelTypes.empty || filteredModelTypes.contains(it.key)]
                .map[it.value.alias]

            combo.items = importAliases
            if (!importAliases.empty) {
                argument.value = importAliases.get(0)
                combo.text = argument.value
            }
        } catch (IllegalArgumentException ex) {
            val decoration = new ControlDecoration(combo, SWT.TOP.bitwiseOr(SWT.LEFT))
            decoration.descriptionText = ex.message
            decoration.image = FieldDecorationRegistry
                .^default
                .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                .image
            decoration.show()
        }
    }

    def setLayoutData (Object layoutData) {
        combo.layoutData = layoutData
    }
}