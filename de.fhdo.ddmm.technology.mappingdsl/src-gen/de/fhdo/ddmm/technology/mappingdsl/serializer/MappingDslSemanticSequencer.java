/*
 * generated by Xtext 2.16.0
 */
package de.fhdo.ddmm.technology.mappingdsl.serializer;

import com.google.inject.Inject;
import de.fhdo.ddmm.data.ComplexTypeImport;
import de.fhdo.ddmm.data.Context;
import de.fhdo.ddmm.data.DataField;
import de.fhdo.ddmm.data.DataModel;
import de.fhdo.ddmm.data.DataPackage;
import de.fhdo.ddmm.data.DataStructure;
import de.fhdo.ddmm.data.Enumeration;
import de.fhdo.ddmm.data.EnumerationField;
import de.fhdo.ddmm.data.ListType;
import de.fhdo.ddmm.data.PossiblyImportedComplexType;
import de.fhdo.ddmm.data.PrimitiveBoolean;
import de.fhdo.ddmm.data.PrimitiveByte;
import de.fhdo.ddmm.data.PrimitiveCharacter;
import de.fhdo.ddmm.data.PrimitiveDate;
import de.fhdo.ddmm.data.PrimitiveDouble;
import de.fhdo.ddmm.data.PrimitiveFloat;
import de.fhdo.ddmm.data.PrimitiveInteger;
import de.fhdo.ddmm.data.PrimitiveLong;
import de.fhdo.ddmm.data.PrimitiveShort;
import de.fhdo.ddmm.data.PrimitiveString;
import de.fhdo.ddmm.data.PrimitiveValue;
import de.fhdo.ddmm.data.Version;
import de.fhdo.ddmm.serializer.ServiceDslSemanticSequencer;
import de.fhdo.ddmm.service.Endpoint;
import de.fhdo.ddmm.service.Import;
import de.fhdo.ddmm.service.ImportedProtocolAndDataFormat;
import de.fhdo.ddmm.service.ImportedServiceAspect;
import de.fhdo.ddmm.service.ImportedType;
import de.fhdo.ddmm.service.Interface;
import de.fhdo.ddmm.service.Microservice;
import de.fhdo.ddmm.service.Operation;
import de.fhdo.ddmm.service.PossiblyImportedInterface;
import de.fhdo.ddmm.service.PossiblyImportedMicroservice;
import de.fhdo.ddmm.service.PossiblyImportedOperation;
import de.fhdo.ddmm.service.ProtocolSpecification;
import de.fhdo.ddmm.service.ReferredOperation;
import de.fhdo.ddmm.service.ServiceModel;
import de.fhdo.ddmm.service.ServicePackage;
import de.fhdo.ddmm.technology.CompatibilityMatrixEntry;
import de.fhdo.ddmm.technology.DataFormat;
import de.fhdo.ddmm.technology.DeploymentTechnology;
import de.fhdo.ddmm.technology.InfrastructureTechnology;
import de.fhdo.ddmm.technology.OperationAspect;
import de.fhdo.ddmm.technology.OperationEnvironment;
import de.fhdo.ddmm.technology.PossiblyImportedTechnologySpecificType;
import de.fhdo.ddmm.technology.Protocol;
import de.fhdo.ddmm.technology.ServiceAspect;
import de.fhdo.ddmm.technology.ServiceAspectPointcut;
import de.fhdo.ddmm.technology.ServiceAspectPointcutSelector;
import de.fhdo.ddmm.technology.Technology;
import de.fhdo.ddmm.technology.TechnologyImport;
import de.fhdo.ddmm.technology.TechnologyPackage;
import de.fhdo.ddmm.technology.TechnologySpecificDataStructure;
import de.fhdo.ddmm.technology.TechnologySpecificListType;
import de.fhdo.ddmm.technology.TechnologySpecificPrimitiveType;
import de.fhdo.ddmm.technology.TechnologySpecificProperty;
import de.fhdo.ddmm.technology.TechnologySpecificPropertyValueAssignment;
import de.fhdo.ddmm.technology.mapping.ComplexParameterMapping;
import de.fhdo.ddmm.technology.mapping.ComplexTypeMapping;
import de.fhdo.ddmm.technology.mapping.ImportedComplexType;
import de.fhdo.ddmm.technology.mapping.ImportedMicroservice;
import de.fhdo.ddmm.technology.mapping.InterfaceMapping;
import de.fhdo.ddmm.technology.mapping.MappingPackage;
import de.fhdo.ddmm.technology.mapping.MicroserviceMapping;
import de.fhdo.ddmm.technology.mapping.OperationMapping;
import de.fhdo.ddmm.technology.mapping.PrimitiveParameterMapping;
import de.fhdo.ddmm.technology.mapping.ReferredOperationMapping;
import de.fhdo.ddmm.technology.mapping.TechnologyMapping;
import de.fhdo.ddmm.technology.mapping.TechnologySpecificEndpoint;
import de.fhdo.ddmm.technology.mapping.TechnologySpecificFieldMapping;
import de.fhdo.ddmm.technology.mapping.TechnologySpecificImportedServiceAspect;
import de.fhdo.ddmm.technology.mapping.TechnologySpecificProtocol;
import de.fhdo.ddmm.technology.mapping.TechnologySpecificProtocolSpecification;
import de.fhdo.ddmm.technology.mappingdsl.services.MappingDslGrammarAccess;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.serializer.ISerializationContext;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class MappingDslSemanticSequencer extends ServiceDslSemanticSequencer {

	@Inject
	private MappingDslGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == DataPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case DataPackage.COMPLEX_TYPE_IMPORT:
				sequence_ComplexTypeImport(context, (ComplexTypeImport) semanticObject); 
				return; 
			case DataPackage.CONTEXT:
				sequence_Context(context, (Context) semanticObject); 
				return; 
			case DataPackage.DATA_FIELD:
				sequence_DataField(context, (DataField) semanticObject); 
				return; 
			case DataPackage.DATA_MODEL:
				sequence_DataModel(context, (DataModel) semanticObject); 
				return; 
			case DataPackage.DATA_STRUCTURE:
				sequence_DataStructure(context, (DataStructure) semanticObject); 
				return; 
			case DataPackage.ENUMERATION:
				sequence_Enumeration(context, (Enumeration) semanticObject); 
				return; 
			case DataPackage.ENUMERATION_FIELD:
				sequence_EnumerationField(context, (EnumerationField) semanticObject); 
				return; 
			case DataPackage.LIST_TYPE:
				sequence_ListType(context, (ListType) semanticObject); 
				return; 
			case DataPackage.POSSIBLY_IMPORTED_COMPLEX_TYPE:
				sequence_PossiblyImportedComplexType(context, (PossiblyImportedComplexType) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_BOOLEAN:
				sequence_PrimitiveType(context, (PrimitiveBoolean) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_BYTE:
				sequence_PrimitiveType(context, (PrimitiveByte) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_CHARACTER:
				sequence_PrimitiveType(context, (PrimitiveCharacter) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_DATE:
				sequence_PrimitiveType(context, (PrimitiveDate) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_DOUBLE:
				sequence_PrimitiveType(context, (PrimitiveDouble) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_FLOAT:
				sequence_PrimitiveType(context, (PrimitiveFloat) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_INTEGER:
				sequence_PrimitiveType(context, (PrimitiveInteger) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_LONG:
				sequence_PrimitiveType(context, (PrimitiveLong) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_SHORT:
				sequence_PrimitiveType(context, (PrimitiveShort) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_STRING:
				sequence_PrimitiveType(context, (PrimitiveString) semanticObject); 
				return; 
			case DataPackage.PRIMITIVE_VALUE:
				sequence_PrimitiveValue(context, (PrimitiveValue) semanticObject); 
				return; 
			case DataPackage.VERSION:
				sequence_Version(context, (Version) semanticObject); 
				return; 
			}
		else if (epackage == MappingPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case MappingPackage.COMPLEX_PARAMETER_MAPPING:
				sequence_ComplexParameterMapping(context, (ComplexParameterMapping) semanticObject); 
				return; 
			case MappingPackage.COMPLEX_TYPE_MAPPING:
				sequence_ComplexTypeMapping(context, (ComplexTypeMapping) semanticObject); 
				return; 
			case MappingPackage.IMPORTED_COMPLEX_TYPE:
				sequence_ImportedComplexType(context, (ImportedComplexType) semanticObject); 
				return; 
			case MappingPackage.IMPORTED_MICROSERVICE:
				sequence_ImportedMicroservice(context, (ImportedMicroservice) semanticObject); 
				return; 
			case MappingPackage.INTERFACE_MAPPING:
				sequence_InterfaceMapping(context, (InterfaceMapping) semanticObject); 
				return; 
			case MappingPackage.MICROSERVICE_MAPPING:
				sequence_MicroserviceMapping(context, (MicroserviceMapping) semanticObject); 
				return; 
			case MappingPackage.OPERATION_MAPPING:
				sequence_OperationMapping(context, (OperationMapping) semanticObject); 
				return; 
			case MappingPackage.PRIMITIVE_PARAMETER_MAPPING:
				sequence_PrimitiveParameterMapping(context, (PrimitiveParameterMapping) semanticObject); 
				return; 
			case MappingPackage.REFERRED_OPERATION_MAPPING:
				sequence_ReferredOperationMapping(context, (ReferredOperationMapping) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_MAPPING:
				sequence_TechnologyMapping(context, (TechnologyMapping) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_SPECIFIC_ENDPOINT:
				sequence_TechnologySpecificEndpoint(context, (TechnologySpecificEndpoint) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_SPECIFIC_FIELD_MAPPING:
				sequence_TechnologySpecificFieldMapping(context, (TechnologySpecificFieldMapping) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_SPECIFIC_IMPORTED_SERVICE_ASPECT:
				sequence_TechnologySpecificImportedServiceAspect(context, (TechnologySpecificImportedServiceAspect) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_SPECIFIC_PROTOCOL:
				sequence_TechnologySpecificProtocol(context, (TechnologySpecificProtocol) semanticObject); 
				return; 
			case MappingPackage.TECHNOLOGY_SPECIFIC_PROTOCOL_SPECIFICATION:
				sequence_TechnologySpecificProtocolSpecification(context, (TechnologySpecificProtocolSpecification) semanticObject); 
				return; 
			}
		else if (epackage == ServicePackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case ServicePackage.ENDPOINT:
				sequence_Endpoint(context, (Endpoint) semanticObject); 
				return; 
			case ServicePackage.IMPORT:
				sequence_Import(context, (Import) semanticObject); 
				return; 
			case ServicePackage.IMPORTED_PROTOCOL_AND_DATA_FORMAT:
				sequence_ImportedProtocolAndDataFormat(context, (ImportedProtocolAndDataFormat) semanticObject); 
				return; 
			case ServicePackage.IMPORTED_SERVICE_ASPECT:
				sequence_ImportedServiceAspect(context, (ImportedServiceAspect) semanticObject); 
				return; 
			case ServicePackage.IMPORTED_TYPE:
				sequence_ImportedType(context, (ImportedType) semanticObject); 
				return; 
			case ServicePackage.INTERFACE:
				sequence_Interface(context, (Interface) semanticObject); 
				return; 
			case ServicePackage.MICROSERVICE:
				sequence_Microservice(context, (Microservice) semanticObject); 
				return; 
			case ServicePackage.OPERATION:
				sequence_Operation(context, (Operation) semanticObject); 
				return; 
			case ServicePackage.PARAMETER:
				sequence_Parameter(context, (de.fhdo.ddmm.service.Parameter) semanticObject); 
				return; 
			case ServicePackage.POSSIBLY_IMPORTED_INTERFACE:
				sequence_PossiblyImportedInterface(context, (PossiblyImportedInterface) semanticObject); 
				return; 
			case ServicePackage.POSSIBLY_IMPORTED_MICROSERVICE:
				sequence_PossiblyImportedMicroservice(context, (PossiblyImportedMicroservice) semanticObject); 
				return; 
			case ServicePackage.POSSIBLY_IMPORTED_OPERATION:
				sequence_PossiblyImportedOperation(context, (PossiblyImportedOperation) semanticObject); 
				return; 
			case ServicePackage.PROTOCOL_SPECIFICATION:
				sequence_ProtocolSpecification(context, (ProtocolSpecification) semanticObject); 
				return; 
			case ServicePackage.REFERRED_OPERATION:
				sequence_ReferredOperation(context, (ReferredOperation) semanticObject); 
				return; 
			case ServicePackage.SERVICE_MODEL:
				sequence_ServiceModel(context, (ServiceModel) semanticObject); 
				return; 
			}
		else if (epackage == TechnologyPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case TechnologyPackage.COMPATIBILITY_MATRIX_ENTRY:
				sequence_CompatibilityMatrixEntry(context, (CompatibilityMatrixEntry) semanticObject); 
				return; 
			case TechnologyPackage.DATA_FORMAT:
				sequence_DataFormat(context, (DataFormat) semanticObject); 
				return; 
			case TechnologyPackage.DEPLOYMENT_TECHNOLOGY:
				sequence_DeploymentTechnology(context, (DeploymentTechnology) semanticObject); 
				return; 
			case TechnologyPackage.INFRASTRUCTURE_TECHNOLOGY:
				sequence_InfrastructureTechnology(context, (InfrastructureTechnology) semanticObject); 
				return; 
			case TechnologyPackage.OPERATION_ASPECT:
				sequence_OperationAspect(context, (OperationAspect) semanticObject); 
				return; 
			case TechnologyPackage.OPERATION_ENVIRONMENT:
				sequence_OperationEnvironment(context, (OperationEnvironment) semanticObject); 
				return; 
			case TechnologyPackage.POSSIBLY_IMPORTED_TECHNOLOGY_SPECIFIC_TYPE:
				sequence_PossiblyImportedTechnologySpecificType(context, (PossiblyImportedTechnologySpecificType) semanticObject); 
				return; 
			case TechnologyPackage.PROTOCOL:
				sequence_Protocol(context, (Protocol) semanticObject); 
				return; 
			case TechnologyPackage.SERVICE_ASPECT:
				sequence_ServiceAspect(context, (ServiceAspect) semanticObject); 
				return; 
			case TechnologyPackage.SERVICE_ASPECT_POINTCUT:
				sequence_ServiceAspectPointcut(context, (ServiceAspectPointcut) semanticObject); 
				return; 
			case TechnologyPackage.SERVICE_ASPECT_POINTCUT_SELECTOR:
				sequence_ServiceAspectPointcutSelector(context, (ServiceAspectPointcutSelector) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY:
				sequence_Technology(context, (Technology) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_IMPORT:
				sequence_TechnologyImport(context, (TechnologyImport) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_SPECIFIC_DATA_STRUCTURE:
				sequence_TechnologySpecificDataStructure(context, (TechnologySpecificDataStructure) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_SPECIFIC_LIST_TYPE:
				sequence_TechnologySpecificListType(context, (TechnologySpecificListType) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_SPECIFIC_PRIMITIVE_TYPE:
				sequence_TechnologySpecificPrimitiveType(context, (TechnologySpecificPrimitiveType) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_SPECIFIC_PROPERTY:
				sequence_TechnologySpecificProperty(context, (TechnologySpecificProperty) semanticObject); 
				return; 
			case TechnologyPackage.TECHNOLOGY_SPECIFIC_PROPERTY_VALUE_ASSIGNMENT:
				sequence_PropertyValueAssignment(context, (TechnologySpecificPropertyValueAssignment) semanticObject); 
				return; 
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     ComplexParameterMapping returns ComplexParameterMapping
	 *
	 * Constraint:
	 *     (
	 *         parameter=[Parameter|ID] 
	 *         (
	 *             ((technology=[Import|ID] technologySpecificComplexType=[ComplexType|QualifiedName])? aspects+=TechnologySpecificImportedServiceAspect*) | 
	 *             (aspects+=TechnologySpecificImportedServiceAspect* fieldMappings+=TechnologySpecificFieldMapping+)
	 *         )
	 *     )
	 */
	protected void sequence_ComplexParameterMapping(ISerializationContext context, ComplexParameterMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ComplexTypeMapping returns ComplexTypeMapping
	 *
	 * Constraint:
	 *     (
	 *         technologies+=[Import|ID]+ 
	 *         type=ImportedComplexType 
	 *         aspects+=TechnologySpecificImportedServiceAspect* 
	 *         fieldMappings+=TechnologySpecificFieldMapping*
	 *     )
	 */
	protected void sequence_ComplexTypeMapping(ISerializationContext context, ComplexTypeMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ImportedComplexType returns ImportedComplexType
	 *
	 * Constraint:
	 *     (serviceModelImport=[Import|ID] dataModelImport=[Import|ID] type=[ComplexType|QualifiedName])
	 */
	protected void sequence_ImportedComplexType(ISerializationContext context, ImportedComplexType semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__SERVICE_MODEL_IMPORT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__SERVICE_MODEL_IMPORT));
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__DATA_MODEL_IMPORT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__DATA_MODEL_IMPORT));
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__TYPE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getImportedComplexTypeAccess().getServiceModelImportImportIDTerminalRuleCall_0_0_1(), semanticObject.eGet(MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__SERVICE_MODEL_IMPORT, false));
		feeder.accept(grammarAccess.getImportedComplexTypeAccess().getDataModelImportImportIDTerminalRuleCall_2_0_1(), semanticObject.eGet(MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__DATA_MODEL_IMPORT, false));
		feeder.accept(grammarAccess.getImportedComplexTypeAccess().getTypeComplexTypeQualifiedNameParserRuleCall_4_0_1(), semanticObject.eGet(MappingPackage.Literals.IMPORTED_COMPLEX_TYPE__TYPE, false));
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     ImportedMicroservice returns ImportedMicroservice
	 *
	 * Constraint:
	 *     (import=[Import|ID] microservice=[Microservice|QualifiedNameWithAtLeastOneLevel])
	 */
	protected void sequence_ImportedMicroservice(ISerializationContext context, ImportedMicroservice semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.IMPORTED_MICROSERVICE__IMPORT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.IMPORTED_MICROSERVICE__IMPORT));
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.IMPORTED_MICROSERVICE__MICROSERVICE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.IMPORTED_MICROSERVICE__MICROSERVICE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getImportedMicroserviceAccess().getImportImportIDTerminalRuleCall_0_0_1(), semanticObject.eGet(MappingPackage.Literals.IMPORTED_MICROSERVICE__IMPORT, false));
		feeder.accept(grammarAccess.getImportedMicroserviceAccess().getMicroserviceMicroserviceQualifiedNameWithAtLeastOneLevelParserRuleCall_2_0_1(), semanticObject.eGet(MappingPackage.Literals.IMPORTED_MICROSERVICE__MICROSERVICE, false));
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     InterfaceMapping returns InterfaceMapping
	 *
	 * Constraint:
	 *     (
	 *         interface=[Interface|ID] 
	 *         protocols+=TechnologySpecificProtocolSpecification* 
	 *         endpoints+=TechnologySpecificEndpoint* 
	 *         aspects+=TechnologySpecificImportedServiceAspect*
	 *     )
	 */
	protected void sequence_InterfaceMapping(ISerializationContext context, InterfaceMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     MicroserviceMapping returns MicroserviceMapping
	 *
	 * Constraint:
	 *     (
	 *         technologies+=[Import|ID]+ 
	 *         microservice=ImportedMicroservice 
	 *         protocols+=TechnologySpecificProtocolSpecification* 
	 *         endpoints+=TechnologySpecificEndpoint* 
	 *         aspects+=TechnologySpecificImportedServiceAspect* 
	 *         interfaceMappings+=InterfaceMapping* 
	 *         referredOperationMappings+=ReferredOperationMapping* 
	 *         operationMappings+=OperationMapping*
	 *     )
	 */
	protected void sequence_MicroserviceMapping(ISerializationContext context, MicroserviceMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     OperationMapping returns OperationMapping
	 *
	 * Constraint:
	 *     (
	 *         operation=[Operation|QualifiedName] 
	 *         protocols+=TechnologySpecificProtocolSpecification* 
	 *         endpoints+=TechnologySpecificEndpoint* 
	 *         aspects+=TechnologySpecificImportedServiceAspect* 
	 *         (parameterMappings+=PrimitiveParameterMapping | parameterMappings+=ComplexParameterMapping)*
	 *     )
	 */
	protected void sequence_OperationMapping(ISerializationContext context, OperationMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     PrimitiveParameterMapping returns PrimitiveParameterMapping
	 *
	 * Constraint:
	 *     (
	 *         parameter=[Parameter|ID] 
	 *         (technology=[Import|ID] primitiveType=[TechnologySpecificPrimitiveType|QualifiedName])? 
	 *         aspects+=TechnologySpecificImportedServiceAspect*
	 *     )
	 */
	protected void sequence_PrimitiveParameterMapping(ISerializationContext context, PrimitiveParameterMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     ReferredOperationMapping returns ReferredOperationMapping
	 *
	 * Constraint:
	 *     (
	 *         operation=[ReferredOperation|QualifiedName] 
	 *         protocols+=TechnologySpecificProtocolSpecification* 
	 *         endpoints+=TechnologySpecificEndpoint* 
	 *         aspects+=TechnologySpecificImportedServiceAspect*
	 *     )
	 */
	protected void sequence_ReferredOperationMapping(ISerializationContext context, ReferredOperationMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TechnologyMapping returns TechnologyMapping
	 *
	 * Constraint:
	 *     (imports+=Import+ typeMappings+=ComplexTypeMapping* serviceMappings+=MicroserviceMapping*)
	 */
	protected void sequence_TechnologyMapping(ISerializationContext context, TechnologyMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TechnologySpecificEndpoint returns TechnologySpecificEndpoint
	 *
	 * Constraint:
	 *     (
	 *         technologySpecificProtocols+=TechnologySpecificProtocol 
	 *         technologySpecificProtocols+=TechnologySpecificProtocol* 
	 *         addresses+=STRING 
	 *         addresses+=STRING*
	 *     )
	 */
	protected void sequence_TechnologySpecificEndpoint(ISerializationContext context, TechnologySpecificEndpoint semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TechnologySpecificFieldMapping returns TechnologySpecificFieldMapping
	 *
	 * Constraint:
	 *     (
	 *         (enumerationField=[EnumerationField|ID] aspects+=TechnologySpecificImportedServiceAspect+) | 
	 *         (dataField=[DataField|ID] (technology=[Import|ID] type=[Type|QualifiedName])? aspects+=TechnologySpecificImportedServiceAspect*)
	 *     )
	 */
	protected void sequence_TechnologySpecificFieldMapping(ISerializationContext context, TechnologySpecificFieldMapping semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TechnologySpecificImportedServiceAspect returns TechnologySpecificImportedServiceAspect
	 *
	 * Constraint:
	 *     (
	 *         technology=[Import|ID] 
	 *         aspect=[ServiceAspect|QualifiedName] 
	 *         (singlePropertyValue=PrimitiveValue | (values+=PropertyValueAssignment values+=PropertyValueAssignment*))?
	 *     )
	 */
	protected void sequence_TechnologySpecificImportedServiceAspect(ISerializationContext context, TechnologySpecificImportedServiceAspect semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TechnologySpecificProtocolSpecification returns TechnologySpecificProtocolSpecification
	 *
	 * Constraint:
	 *     (communicationType=CommunicationType technologySpecificProtocol=TechnologySpecificProtocol)
	 */
	protected void sequence_TechnologySpecificProtocolSpecification(ISerializationContext context, TechnologySpecificProtocolSpecification semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.TECHNOLOGY_SPECIFIC_PROTOCOL_SPECIFICATION__COMMUNICATION_TYPE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.TECHNOLOGY_SPECIFIC_PROTOCOL_SPECIFICATION__COMMUNICATION_TYPE));
			if (transientValues.isValueTransient(semanticObject, MappingPackage.Literals.TECHNOLOGY_SPECIFIC_PROTOCOL_SPECIFICATION__TECHNOLOGY_SPECIFIC_PROTOCOL) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, MappingPackage.Literals.TECHNOLOGY_SPECIFIC_PROTOCOL_SPECIFICATION__TECHNOLOGY_SPECIFIC_PROTOCOL));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getTechnologySpecificProtocolSpecificationAccess().getCommunicationTypeCommunicationTypeEnumRuleCall_0_0(), semanticObject.getCommunicationType());
		feeder.accept(grammarAccess.getTechnologySpecificProtocolSpecificationAccess().getTechnologySpecificProtocolTechnologySpecificProtocolParserRuleCall_2_0(), semanticObject.getTechnologySpecificProtocol());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     TechnologySpecificProtocol returns TechnologySpecificProtocol
	 *
	 * Constraint:
	 *     (technology=[Import|ID] protocol=[Protocol|QualifiedName] dataFormat=[DataFormat|ID]?)
	 */
	protected void sequence_TechnologySpecificProtocol(ISerializationContext context, TechnologySpecificProtocol semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
}
