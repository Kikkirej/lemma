/**
 * generated by Xtext 2.16.0
 */
package de.fhdo.lemma.operationdsl.tests;

import com.google.inject.Inject;
import de.fhdo.lemma.operation.OperationModel;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.XtextRunner;
import org.eclipse.xtext.testing.util.ParseHelper;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(OperationDslInjectorProvider.class)
@SuppressWarnings("all")
public class OperationDslParsingTest {
  @Inject
  private ParseHelper<OperationModel> parseHelper;
  
  @Test
  public void loadModel() {
  }
}
