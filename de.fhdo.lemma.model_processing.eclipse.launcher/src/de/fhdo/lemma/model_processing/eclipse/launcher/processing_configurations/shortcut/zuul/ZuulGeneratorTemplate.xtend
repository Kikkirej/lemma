package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.zuul

import org.eclipse.swt.widgets.Shell
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessorExecutableType
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.shortcut.AbstractCodeGeneratorCompletionDialog

class ZuulGeneratorTemplate extends AbstractZuulGeneratorTemplate {
    new(Shell parentShell, IProject project, IFile file) {
        this(parentShell, "Zuul artifact generation", project, file)
    }

    new(Shell parentShell, String name, IProject project, IFile file) {
        super(parentShell, name, project, file)
    }

    override getProcessorExecutableType() {
        return ProcessorExecutableType.LOCAL_JAVA_PROGRAM
    }

    override getCodeGeneratorCompletionDialog() {
        return new TemplateCompletionDialog(parentShell, project, file)
    }

    static class TemplateCompletionDialog extends AbstractCodeGeneratorCompletionDialog {
        protected new(Shell parentShell, IProject project, IFile file) {
            super(parentShell, project, file, ZuulGeneratorConstants.GENERATOR_LONG_NAME,
                ZuulGeneratorConstants.GENERATOR_SHORT_NAME)
        }

        override getProcessorExecutableLabelTextAddendum() {
            return "path"
        }

        override create() {
            super.create()
            title = "Generate Zuul Artifacts From LEMMA Models"
            message = '''Use this dialog to configure and run LEMMA's «generatorLongName» on ''' +
                "the selected operation model"
        }
    }
}