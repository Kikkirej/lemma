package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations

import org.eclipse.ui.console.MessageConsoleStream
import de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ProcessingConfiguration
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import de.fhdo.lemma.eclipse.ui.ModelFile
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationException
import java.util.List
import de.fhdo.lemma.eclipse.ui.ProgrammaticIntermediateModelTransformation.ProgrammaticIntermediateModelTransformationResult
import org.eclipse.core.runtime.SubMonitor
import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*

class IntermediateTransformationExecutor {
    val ProcessingConfiguration configuration
    val MessageConsoleStream infoStream
    val MessageConsoleStream errorStream
    val ()=>void successfulCallback
    val ()=>void errorOrCancelCallback
    var IProgressMonitor monitor
    var boolean stopTransformations

    new(
        ProcessingConfiguration configuration,
        MessageConsoleStream infoStream,
        MessageConsoleStream errorStream,
        ()=>void successfulCallback,
        ()=>void errorOrCancelCallback
    ) {
        this.infoStream = infoStream
        this.errorStream = errorStream
        this.configuration = configuration
        this.successfulCallback = successfulCallback
        this.errorOrCancelCallback = errorOrCancelCallback
    }

    def run() {
        val job = new Job("LEMMA intermediate transformation") {
            override IStatus run(IProgressMonitor monitor) {
                val subMonitor = SubMonitor.convert(monitor, 1);
                IntermediateTransformationExecutor.this.monitor = subMonitor

                new ProgrammaticIntermediateModelTransformation(configuration.sourceModelFile).run(
                    "PROCESSING_CONFIGURATION_EXECUTION",
                    null,
                    null,
                    true,
                    [nextIntermediateTransformation],
                    [intermediateTransformationException],
                    [transformationSuccessful],
                    [results, exceptions | transformationsFinished(results, exceptions)]
                )

                return if (!subMonitor.canceled)
                        Status.OK_STATUS
                    else
                        Status.CANCEL_STATUS
            }
        }
        job.schedule()
    }

    private def boolean nextIntermediateTransformation(ModelFile modelFile) {
        if (stopTransformations())
            return false

        infoStream.println("Running intermediate transformation of model file " +
            '''"«modelFile.file.project.fullPath»/«modelFile.file.projectRelativePath»"''')
        return !canceledByUser()
    }

    private def stopTransformations() {
        if (stopTransformations)
            return true
        else if (canceledByUser()) {
            stopTransformations = true
            return true
        } else
            return false
    }

    private def canceledByUser() {
        if (monitor.isCanceled) {
            errorStream.println("Transformations aborted by user")
            errorOrCancelCallback.apply()
        }

        return monitor.isCanceled
    }

    private def boolean intermediateTransformationException(
        ProgrammaticIntermediateModelTransformationException ex
    ) {
        if (!stopTransformations()) {
            errorStream.println("\t" + '''Error: «ex.message»''' + "\n\tTransformations aborted.")
            stopTransformations = true
        }
        monitor.worked(1)
        errorOrCancelCallback.apply()
        return false
    }

    private def boolean transformationSuccessful(
        List<ProgrammaticIntermediateModelTransformationResult> results
    ) {
        if (stopTransformations())
            return false

        printlnIndent(infoStream, "Successful.")
        infoStream.println()

        return !canceledByUser()
    }

    private def getUniqueOutputPaths(List<ProgrammaticIntermediateModelTransformationResult> results) {
        return results.map[it.result.outputModel.outputPath].toSet
    }

    private def boolean transformationsFinished(
        List<ProgrammaticIntermediateModelTransformationResult> allResults,
        List<ProgrammaticIntermediateModelTransformationException> allExceptions
    ) {
        if (stopTransformations())
            return false

        val resultCount = allResults.uniqueOutputPaths.size
        val resultsSuffix = resultCount === 1 ? "result file" : "result files"
        val errorCount = allExceptions.size
        val errorsSuffix = errorCount === 1 ? "error" : "errors"

        if (!allExceptions.empty)
            infoStream.println("Transformations finished " +
                '''(«errorCount» «errorsSuffix», «resultCount» «resultsSuffix» from ''' +
                "successful transformations)")
        else
            infoStream.println("All transformations finished successfully " +
                '''(«resultCount» «resultsSuffix»)''')

        if (!allResults.empty) {
            printlnIndent(infoStream, "Resulting model files:")
            // Filter duplicate output paths, which can occur from refinement transformations
            allResults.uniqueOutputPaths.sort.forEach[printlnIndent(infoStream, '''- «it»''', 2)]
        }

        if (allExceptions.empty && !canceledByUser())
            successfulCallback.apply()

        monitor.worked(1)
        return true
    }
}