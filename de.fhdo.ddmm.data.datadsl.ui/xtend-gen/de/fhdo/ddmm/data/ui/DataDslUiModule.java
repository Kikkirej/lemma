/**
 * generated by Xtext 2.12.0
 */
package de.fhdo.ddmm.data.ui;

import de.fhdo.ddmm.data.ui.AbstractDataDslUiModule;
import de.fhdo.ddmm.data.ui.highlighting.HighlightingCalculator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.ide.editor.syntaxcoloring.ISemanticHighlightingCalculator;

/**
 * Use this class to register components to be used within the Eclipse IDE.
 * 
 * @author <a href="mailto:florian.rademacher@fh-dortmund.de>Florian Rademacher</a>
 */
@FinalFieldsConstructor
@SuppressWarnings("all")
public class DataDslUiModule extends AbstractDataDslUiModule {
  public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
    return HighlightingCalculator.class;
  }
  
  public DataDslUiModule(final AbstractUIPlugin plugin) {
    super(plugin);
  }
}
