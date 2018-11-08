/**
 */
package de.fhdo.ddmm.operation.impl;

import de.fhdo.ddmm.operation.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OperationFactoryImpl extends EFactoryImpl implements OperationFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static OperationFactory init() {
        try {
            OperationFactory theOperationFactory = (OperationFactory)EPackage.Registry.INSTANCE.getEFactory(OperationPackage.eNS_URI);
            if (theOperationFactory != null) {
                return theOperationFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new OperationFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case OperationPackage.OPERATION_MODEL: return createOperationModel();
            case OperationPackage.CONTAINER: return createContainer();
            case OperationPackage.INFRASTRUCTURE_NODE: return createInfrastructureNode();
            case OperationPackage.IMPORTED_MICROSERVICE: return createImportedMicroservice();
            case OperationPackage.SERVICE_DEPLOYMENT_SPECIFICATION: return createServiceDeploymentSpecification();
            case OperationPackage.PROTOCOL_AND_DATA_FORMAT: return createProtocolAndDataFormat();
            case OperationPackage.BASIC_ENDPOINT: return createBasicEndpoint();
            case OperationPackage.IMPORTED_OPERATION_ASPECT: return createImportedOperationAspect();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case OperationPackage.IMPORT_TYPE:
                return createImportTypeFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case OperationPackage.IMPORT_TYPE:
                return convertImportTypeToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationModel createOperationModel() {
        OperationModelImpl operationModel = new OperationModelImpl();
        return operationModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public de.fhdo.ddmm.operation.Container createContainer() {
        ContainerImpl container = new ContainerImpl();
        return container;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InfrastructureNode createInfrastructureNode() {
        InfrastructureNodeImpl infrastructureNode = new InfrastructureNodeImpl();
        return infrastructureNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImportedMicroservice createImportedMicroservice() {
        ImportedMicroserviceImpl importedMicroservice = new ImportedMicroserviceImpl();
        return importedMicroservice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ServiceDeploymentSpecification createServiceDeploymentSpecification() {
        ServiceDeploymentSpecificationImpl serviceDeploymentSpecification = new ServiceDeploymentSpecificationImpl();
        return serviceDeploymentSpecification;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ProtocolAndDataFormat createProtocolAndDataFormat() {
        ProtocolAndDataFormatImpl protocolAndDataFormat = new ProtocolAndDataFormatImpl();
        return protocolAndDataFormat;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BasicEndpoint createBasicEndpoint() {
        BasicEndpointImpl basicEndpoint = new BasicEndpointImpl();
        return basicEndpoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImportedOperationAspect createImportedOperationAspect() {
        ImportedOperationAspectImpl importedOperationAspect = new ImportedOperationAspectImpl();
        return importedOperationAspect;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ImportType createImportTypeFromString(EDataType eDataType, String initialValue) {
        ImportType result = ImportType.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertImportTypeToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationPackage getOperationPackage() {
        return (OperationPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static OperationPackage getPackage() {
        return OperationPackage.eINSTANCE;
    }

} //OperationFactoryImpl