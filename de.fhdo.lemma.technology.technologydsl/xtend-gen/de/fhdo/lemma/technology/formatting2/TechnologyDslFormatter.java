/**
 * generated by Xtext 2.12.0
 */
package de.fhdo.lemma.technology.formatting2;

import com.google.inject.Inject;
import de.fhdo.lemma.data.DataModel;
import de.fhdo.lemma.data.PrimitiveType;
import de.fhdo.lemma.data.Version;
import de.fhdo.lemma.data.formatting2.DataDslFormatter;
import de.fhdo.lemma.technology.Technology;
import de.fhdo.lemma.technology.TechnologySpecificCollectionType;
import de.fhdo.lemma.technology.TechnologySpecificDataStructure;
import de.fhdo.lemma.technology.TechnologySpecificPrimitiveType;
import de.fhdo.lemma.technology.services.TechnologyDslGrammarAccess;
import java.util.Arrays;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class TechnologyDslFormatter extends DataDslFormatter {
  @Inject
  @Extension
  private TechnologyDslGrammarAccess _technologyDslGrammarAccess;
  
  protected void _format(final Technology technology, @Extension final IFormattableDocument document) {
    EList<TechnologySpecificPrimitiveType> _primitiveTypes = technology.getPrimitiveTypes();
    for (final TechnologySpecificPrimitiveType technologySpecificPrimitiveType : _primitiveTypes) {
      document.<TechnologySpecificPrimitiveType>format(technologySpecificPrimitiveType);
    }
    EList<TechnologySpecificCollectionType> _collectionTypes = technology.getCollectionTypes();
    for (final TechnologySpecificCollectionType technologySpecificCollectionType : _collectionTypes) {
      document.<TechnologySpecificCollectionType>format(technologySpecificCollectionType);
    }
    EList<TechnologySpecificDataStructure> _dataStructures = technology.getDataStructures();
    for (final TechnologySpecificDataStructure technologySpecificDataStructureType : _dataStructures) {
      document.<TechnologySpecificDataStructure>format(technologySpecificDataStructureType);
    }
  }
  
  protected void _format(final TechnologySpecificPrimitiveType technologySpecificPrimitiveType, @Extension final IFormattableDocument document) {
    EList<PrimitiveType> _basicBuiltinPrimitiveTypes = technologySpecificPrimitiveType.getBasicBuiltinPrimitiveTypes();
    for (final PrimitiveType primitiveType : _basicBuiltinPrimitiveTypes) {
      document.<PrimitiveType>format(primitiveType);
    }
  }
  
  public void format(final Object technologySpecificPrimitiveType, final IFormattableDocument document) {
    if (technologySpecificPrimitiveType instanceof TechnologySpecificPrimitiveType) {
      _format((TechnologySpecificPrimitiveType)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType instanceof XtextResource) {
      _format((XtextResource)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType instanceof DataModel) {
      _format((DataModel)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType instanceof Version) {
      _format((Version)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType instanceof Technology) {
      _format((Technology)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType instanceof EObject) {
      _format((EObject)technologySpecificPrimitiveType, document);
      return;
    } else if (technologySpecificPrimitiveType == null) {
      _format((Void)null, document);
      return;
    } else if (technologySpecificPrimitiveType != null) {
      _format(technologySpecificPrimitiveType, document);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(technologySpecificPrimitiveType, document).toString());
    }
  }
}
