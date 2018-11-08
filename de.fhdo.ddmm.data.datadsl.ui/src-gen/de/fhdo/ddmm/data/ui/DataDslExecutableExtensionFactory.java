/*
 * generated by Xtext 2.12.0
 */
package de.fhdo.ddmm.data.ui;

import com.google.inject.Injector;
import de.fhdo.ddmm.data.datadsl.ui.internal.DatadslActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class DataDslExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return DatadslActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return DatadslActivator.getInstance().getInjector(DatadslActivator.DE_FHDO_DDMM_DATA_DATADSL);
	}
	
}