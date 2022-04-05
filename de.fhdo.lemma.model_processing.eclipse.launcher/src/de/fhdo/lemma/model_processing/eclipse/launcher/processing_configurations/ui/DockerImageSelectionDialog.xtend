package de.fhdo.lemma.model_processing.eclipse.launcher.processing_configurations.ui

import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Composite

import org.eclipse.jface.dialogs.Dialog

import static de.fhdo.lemma.model_processing.eclipse.launcher.Utils.*
import static org.apache.commons.lang.StringUtils.*
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.swt.layout.GridData
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Point
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.window.Window
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.jface.viewers.IStructuredSelection
import java.io.IOException

class DockerImageSelectionDialog extends Dialog {
    static val IMAGE_TAG_SEPARATOR = ":"

    val images = <Pair<String, String>>newArrayList
    val nonParseableImageQueryResults = <String>newArrayList
    var TableViewer viewer
    @Accessors(PUBLIC_GETTER)
    var String selectedImage = null

    new(Shell parentShell, String basicDockerCommand) {
        super(parentShell)

        if (basicDockerCommand.nullOrEmpty)
            throw new IllegalArgumentException("Docker selection dialog requires basic Docker " +
                "command but none was given")

        val imageQueryCommand = '''«basicDockerCommand» image ls --format ''' +
            "'{{.Repository}}" + IMAGE_TAG_SEPARATOR + "{{.Tag}}'"
        val imageQueryResult = try {
            executeShellCommandBlocking(imageQueryCommand, 100, 10)
        } catch (IOException ex) {
            throw new IllegalArgumentException("Available Docker images not determinable: " +
                '''Invalid query command "«imageQueryCommand»"''')
        }

        if (imageQueryResult.key != 0)
            throw new IllegalArgumentException("Available Docker images not determinable. " +
                '''Command "«imageQueryCommand»" returned with exit code ''' +
                '''«imageQueryResult.key»: «imageQueryResult.value»''')

        imageQueryResult.value.lines.sorted.forEach[
            // Remove possible apostrophes at the beginning and end of a query result. Their
            // occurrence can result from the image query command and is shell-dependent.
            val result = removeEnd(removeStart(it, "'"), "'")
            val image = substringBeforeLast(result, IMAGE_TAG_SEPARATOR)
            val tag = substringAfterLast(result, IMAGE_TAG_SEPARATOR)
            if (!image.nullOrEmpty && !tag.nullOrEmpty)
                images.add(image -> tag)
            else
                nonParseableImageQueryResults.add(it)
        ]
    }

    override createDialogArea(Composite parent) {
        val container = super.createDialogArea(parent) as Composite

        viewer = new TableViewer(container)
        viewer.contentProvider = new IStructuredContentProvider() {
            override getElements(Object inputElement) {
                return images
            }
        }
        viewer.addDoubleClickListener([
            val selection = it.selection as IStructuredSelection
            val imageAndTag = selection?.firstElement as Pair<String, String>
            if (imageAndTag !== null)
                okPressedFor(imageAndTag)
        ])
        setViewerLayout()

        createImageColumn()
        createTagColumn()

        viewer.input = images

        return container
    }

    private def setViewerLayout() {
        viewer.table.headerVisible = true
        viewer.table.linesVisible = true
        viewer.table.layoutData = new GridData(GridData.FILL, GridData.FILL, true, true)
    }

    private def createImageColumn() {
        val column = new TableViewerColumn(viewer, SWT.NONE)
        column.column.width = 300
        column.column.text = "Image"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Pair<String, String>).key
            }
        }
    }

    private def createTagColumn() {
        val column = new TableViewerColumn(viewer, SWT.NONE)
        column.column.width = 150
        column.column.text = "Tag"
        column.labelProvider = new ColumnLabelProvider() {
            override getText(Object element) {
                return (element as Pair<String, String>).value
            }
        }
    }

    override open() {
        if (images.empty) {
            MessageDialog.openError(shell, "No Docker Images", "No local Docker images found")
            return Window.CANCEL
        }

        if (!nonParseableImageQueryResults.empty)
            MessageDialog.openWarning(shell, "Non-Parseable Docker Images", "The image or tag " +
                "names of the following local Docker images could not be parsed:\n" +
                nonParseableImageQueryResults.map['''\t- «it»'''].join("\n") + "\nThey will be " +
                "missing in the list of selectable images")

        return super.open()
    }

    override okPressed() {
        val selectedRow = singleRowSelectionOrError(viewer)?.firstElement as Pair<String, String>
        if (selectedRow !== null)
            okPressedFor(selectedRow)
    }

    private def okPressedFor(Pair<String, String> imageAndTag) {
        selectedImage = '''«imageAndTag.key»:«imageAndTag.value»'''
        super.okPressed()
    }

    override cancelPressed() {
        selectedImage = null
        super.cancelPressed()
    }

    override configureShell(Shell shell) {
        super.configureShell(shell)
        shell.text = "Docker Image Selection"
    }

    override isResizable() {
        return true
    }

    override getInitialSize() {
        return new Point(600, 700)
    }
}