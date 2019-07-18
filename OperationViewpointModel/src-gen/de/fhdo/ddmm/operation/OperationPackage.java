/**
 */
package de.fhdo.ddmm.operation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.fhdo.ddmm.operation.OperationFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/emf/2002/GenModel basePackage='de.fhdo.ddmm'"
 * @generated
 */
public interface OperationPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "operation";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "de.fhdo.ddmm.operation";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "operation";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    OperationPackage eINSTANCE = de.fhdo.ddmm.operation.impl.OperationPackageImpl.init();

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.OperationModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.OperationModelImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getOperationModel()
     * @generated
     */
    int OPERATION_MODEL = 0;

    /**
     * The feature id for the '<em><b>Imports</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_MODEL__IMPORTS = 0;

    /**
     * The feature id for the '<em><b>Containers</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_MODEL__CONTAINERS = 1;

    /**
     * The feature id for the '<em><b>Infrastructure Nodes</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_MODEL__INFRASTRUCTURE_NODES = 2;

    /**
     * The number of structural features of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_MODEL_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Model</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_MODEL_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.OperationNodeImpl <em>Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.OperationNodeImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getOperationNode()
     * @generated
     */
    int OPERATION_NODE = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__NAME = 0;

    /**
     * The feature id for the '<em><b>Technologies</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__TECHNOLOGIES = 1;

    /**
     * The feature id for the '<em><b>Operation Environment</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__OPERATION_ENVIRONMENT = 2;

    /**
     * The feature id for the '<em><b>Deployed Services</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__DEPLOYED_SERVICES = 3;

    /**
     * The feature id for the '<em><b>Default Service Property Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__DEFAULT_SERVICE_PROPERTY_VALUES = 4;

    /**
     * The feature id for the '<em><b>Deployment Specifications</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__DEPLOYMENT_SPECIFICATIONS = 5;

    /**
     * The feature id for the '<em><b>Aspects</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE__ASPECTS = 6;

    /**
     * The number of structural features of the '<em>Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OPERATION_NODE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.ContainerImpl <em>Container</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.ContainerImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getContainer()
     * @generated
     */
    int CONTAINER = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__NAME = OPERATION_NODE__NAME;

    /**
     * The feature id for the '<em><b>Technologies</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__TECHNOLOGIES = OPERATION_NODE__TECHNOLOGIES;

    /**
     * The feature id for the '<em><b>Operation Environment</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__OPERATION_ENVIRONMENT = OPERATION_NODE__OPERATION_ENVIRONMENT;

    /**
     * The feature id for the '<em><b>Deployed Services</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__DEPLOYED_SERVICES = OPERATION_NODE__DEPLOYED_SERVICES;

    /**
     * The feature id for the '<em><b>Default Service Property Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__DEFAULT_SERVICE_PROPERTY_VALUES = OPERATION_NODE__DEFAULT_SERVICE_PROPERTY_VALUES;

    /**
     * The feature id for the '<em><b>Deployment Specifications</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__DEPLOYMENT_SPECIFICATIONS = OPERATION_NODE__DEPLOYMENT_SPECIFICATIONS;

    /**
     * The feature id for the '<em><b>Aspects</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__ASPECTS = OPERATION_NODE__ASPECTS;

    /**
     * The feature id for the '<em><b>Deployment Technology</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__DEPLOYMENT_TECHNOLOGY = OPERATION_NODE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Default Basic Endpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__DEFAULT_BASIC_ENDPOINTS = OPERATION_NODE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Operation Model</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER__OPERATION_MODEL = OPERATION_NODE_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Container</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER_FEATURE_COUNT = OPERATION_NODE_FEATURE_COUNT + 3;

    /**
     * The number of operations of the '<em>Container</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONTAINER_OPERATION_COUNT = OPERATION_NODE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.DeploymentTechnologyReferenceImpl <em>Deployment Technology Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.DeploymentTechnologyReferenceImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getDeploymentTechnologyReference()
     * @generated
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE = 3;

    /**
     * The feature id for the '<em><b>Import</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE__IMPORT = 0;

    /**
     * The feature id for the '<em><b>Deployment Technology</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE__DEPLOYMENT_TECHNOLOGY = 1;

    /**
     * The feature id for the '<em><b>Container</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE__CONTAINER = 2;

    /**
     * The number of structural features of the '<em>Deployment Technology Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Deployment Technology Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int DEPLOYMENT_TECHNOLOGY_REFERENCE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.InfrastructureNodeImpl <em>Infrastructure Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.InfrastructureNodeImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getInfrastructureNode()
     * @generated
     */
    int INFRASTRUCTURE_NODE = 4;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__NAME = OPERATION_NODE__NAME;

    /**
     * The feature id for the '<em><b>Technologies</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__TECHNOLOGIES = OPERATION_NODE__TECHNOLOGIES;

    /**
     * The feature id for the '<em><b>Operation Environment</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__OPERATION_ENVIRONMENT = OPERATION_NODE__OPERATION_ENVIRONMENT;

    /**
     * The feature id for the '<em><b>Deployed Services</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__DEPLOYED_SERVICES = OPERATION_NODE__DEPLOYED_SERVICES;

    /**
     * The feature id for the '<em><b>Default Service Property Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__DEFAULT_SERVICE_PROPERTY_VALUES = OPERATION_NODE__DEFAULT_SERVICE_PROPERTY_VALUES;

    /**
     * The feature id for the '<em><b>Deployment Specifications</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__DEPLOYMENT_SPECIFICATIONS = OPERATION_NODE__DEPLOYMENT_SPECIFICATIONS;

    /**
     * The feature id for the '<em><b>Aspects</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__ASPECTS = OPERATION_NODE__ASPECTS;

    /**
     * The feature id for the '<em><b>Used By Nodes</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__USED_BY_NODES = OPERATION_NODE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Depends On Nodes</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__DEPENDS_ON_NODES = OPERATION_NODE_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Infrastructure Technology</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__INFRASTRUCTURE_TECHNOLOGY = OPERATION_NODE_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Endpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__ENDPOINTS = OPERATION_NODE_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Operation Model</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE__OPERATION_MODEL = OPERATION_NODE_FEATURE_COUNT + 4;

    /**
     * The number of structural features of the '<em>Infrastructure Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE_FEATURE_COUNT = OPERATION_NODE_FEATURE_COUNT + 5;

    /**
     * The number of operations of the '<em>Infrastructure Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_NODE_OPERATION_COUNT = OPERATION_NODE_OPERATION_COUNT + 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.InfrastructureTechnologyReferenceImpl <em>Infrastructure Technology Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.InfrastructureTechnologyReferenceImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getInfrastructureTechnologyReference()
     * @generated
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE = 5;

    /**
     * The feature id for the '<em><b>Import</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE__IMPORT = 0;

    /**
     * The feature id for the '<em><b>Infrastructure Technology</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE__INFRASTRUCTURE_TECHNOLOGY = 1;

    /**
     * The feature id for the '<em><b>Infrastructure Node</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE__INFRASTRUCTURE_NODE = 2;

    /**
     * The number of structural features of the '<em>Infrastructure Technology Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Infrastructure Technology Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int INFRASTRUCTURE_TECHNOLOGY_REFERENCE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.ImportedMicroserviceImpl <em>Imported Microservice</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.ImportedMicroserviceImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportedMicroservice()
     * @generated
     */
    int IMPORTED_MICROSERVICE = 6;

    /**
     * The feature id for the '<em><b>Import</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_MICROSERVICE__IMPORT = 0;

    /**
     * The feature id for the '<em><b>Microservice</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_MICROSERVICE__MICROSERVICE = 1;

    /**
     * The feature id for the '<em><b>Operation Node</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_MICROSERVICE__OPERATION_NODE = 2;

    /**
     * The number of structural features of the '<em>Imported Microservice</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_MICROSERVICE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Imported Microservice</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_MICROSERVICE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.ServiceDeploymentSpecificationImpl <em>Service Deployment Specification</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.ServiceDeploymentSpecificationImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getServiceDeploymentSpecification()
     * @generated
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION = 7;

    /**
     * The feature id for the '<em><b>Import</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION__IMPORT = 0;

    /**
     * The feature id for the '<em><b>Service</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION__SERVICE = 1;

    /**
     * The feature id for the '<em><b>Service Property Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION__SERVICE_PROPERTY_VALUES = 2;

    /**
     * The feature id for the '<em><b>Basic Endpoints</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION__BASIC_ENDPOINTS = 3;

    /**
     * The feature id for the '<em><b>Operation Node</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION__OPERATION_NODE = 4;

    /**
     * The number of structural features of the '<em>Service Deployment Specification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Service Deployment Specification</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SERVICE_DEPLOYMENT_SPECIFICATION_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.ProtocolAndDataFormatImpl <em>Protocol And Data Format</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.ProtocolAndDataFormatImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getProtocolAndDataFormat()
     * @generated
     */
    int PROTOCOL_AND_DATA_FORMAT = 8;

    /**
     * The feature id for the '<em><b>Technology</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT__TECHNOLOGY = 0;

    /**
     * The feature id for the '<em><b>Protocol</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT__PROTOCOL = 1;

    /**
     * The feature id for the '<em><b>Data Format</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT__DATA_FORMAT = 2;

    /**
     * The feature id for the '<em><b>Endpoint</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT__ENDPOINT = 3;

    /**
     * The number of structural features of the '<em>Protocol And Data Format</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Protocol And Data Format</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PROTOCOL_AND_DATA_FORMAT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.BasicEndpointImpl <em>Basic Endpoint</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.BasicEndpointImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getBasicEndpoint()
     * @generated
     */
    int BASIC_ENDPOINT = 9;

    /**
     * The feature id for the '<em><b>Addresses</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT__ADDRESSES = 0;

    /**
     * The feature id for the '<em><b>Protocols</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT__PROTOCOLS = 1;

    /**
     * The feature id for the '<em><b>Container</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT__CONTAINER = 2;

    /**
     * The feature id for the '<em><b>Infrastructure Node</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT__INFRASTRUCTURE_NODE = 3;

    /**
     * The feature id for the '<em><b>Deployment Specification</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT__DEPLOYMENT_SPECIFICATION = 4;

    /**
     * The number of structural features of the '<em>Basic Endpoint</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Basic Endpoint</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int BASIC_ENDPOINT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl <em>Imported Operation Aspect</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportedOperationAspect()
     * @generated
     */
    int IMPORTED_OPERATION_ASPECT = 10;

    /**
     * The feature id for the '<em><b>Technology</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT__TECHNOLOGY = 0;

    /**
     * The feature id for the '<em><b>Aspect</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT__ASPECT = 1;

    /**
     * The feature id for the '<em><b>Single Property Value</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE = 2;

    /**
     * The feature id for the '<em><b>Values</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT__VALUES = 3;

    /**
     * The feature id for the '<em><b>Operation Node</b></em>' container reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT__OPERATION_NODE = 4;

    /**
     * The number of structural features of the '<em>Imported Operation Aspect</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Imported Operation Aspect</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int IMPORTED_OPERATION_ASPECT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link de.fhdo.ddmm.operation.ImportType <em>Import Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see de.fhdo.ddmm.operation.ImportType
     * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportType()
     * @generated
     */
    int IMPORT_TYPE = 11;


    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.OperationModel <em>Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model</em>'.
     * @see de.fhdo.ddmm.operation.OperationModel
     * @generated
     */
    EClass getOperationModel();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationModel#getImports <em>Imports</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Imports</em>'.
     * @see de.fhdo.ddmm.operation.OperationModel#getImports()
     * @see #getOperationModel()
     * @generated
     */
    EReference getOperationModel_Imports();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationModel#getContainers <em>Containers</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Containers</em>'.
     * @see de.fhdo.ddmm.operation.OperationModel#getContainers()
     * @see #getOperationModel()
     * @generated
     */
    EReference getOperationModel_Containers();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationModel#getInfrastructureNodes <em>Infrastructure Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Infrastructure Nodes</em>'.
     * @see de.fhdo.ddmm.operation.OperationModel#getInfrastructureNodes()
     * @see #getOperationModel()
     * @generated
     */
    EReference getOperationModel_InfrastructureNodes();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.OperationNode <em>Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Node</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode
     * @generated
     */
    EClass getOperationNode();

    /**
     * Returns the meta object for the attribute '{@link de.fhdo.ddmm.operation.OperationNode#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getName()
     * @see #getOperationNode()
     * @generated
     */
    EAttribute getOperationNode_Name();

    /**
     * Returns the meta object for the reference list '{@link de.fhdo.ddmm.operation.OperationNode#getTechnologies <em>Technologies</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Technologies</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getTechnologies()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_Technologies();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.OperationNode#getOperationEnvironment <em>Operation Environment</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Operation Environment</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getOperationEnvironment()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_OperationEnvironment();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationNode#getDeployedServices <em>Deployed Services</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Deployed Services</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getDeployedServices()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_DeployedServices();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationNode#getDefaultServicePropertyValues <em>Default Service Property Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Default Service Property Values</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getDefaultServicePropertyValues()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_DefaultServicePropertyValues();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationNode#getDeploymentSpecifications <em>Deployment Specifications</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Deployment Specifications</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getDeploymentSpecifications()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_DeploymentSpecifications();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.OperationNode#getAspects <em>Aspects</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Aspects</em>'.
     * @see de.fhdo.ddmm.operation.OperationNode#getAspects()
     * @see #getOperationNode()
     * @generated
     */
    EReference getOperationNode_Aspects();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.Container <em>Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Container</em>'.
     * @see de.fhdo.ddmm.operation.Container
     * @generated
     */
    EClass getContainer();

    /**
     * Returns the meta object for the containment reference '{@link de.fhdo.ddmm.operation.Container#getDeploymentTechnology <em>Deployment Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Deployment Technology</em>'.
     * @see de.fhdo.ddmm.operation.Container#getDeploymentTechnology()
     * @see #getContainer()
     * @generated
     */
    EReference getContainer_DeploymentTechnology();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.Container#getDefaultBasicEndpoints <em>Default Basic Endpoints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Default Basic Endpoints</em>'.
     * @see de.fhdo.ddmm.operation.Container#getDefaultBasicEndpoints()
     * @see #getContainer()
     * @generated
     */
    EReference getContainer_DefaultBasicEndpoints();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.Container#getOperationModel <em>Operation Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Operation Model</em>'.
     * @see de.fhdo.ddmm.operation.Container#getOperationModel()
     * @see #getContainer()
     * @generated
     */
    EReference getContainer_OperationModel();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.DeploymentTechnologyReference <em>Deployment Technology Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Deployment Technology Reference</em>'.
     * @see de.fhdo.ddmm.operation.DeploymentTechnologyReference
     * @generated
     */
    EClass getDeploymentTechnologyReference();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.DeploymentTechnologyReference#getImport <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Import</em>'.
     * @see de.fhdo.ddmm.operation.DeploymentTechnologyReference#getImport()
     * @see #getDeploymentTechnologyReference()
     * @generated
     */
    EReference getDeploymentTechnologyReference_Import();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.DeploymentTechnologyReference#getDeploymentTechnology <em>Deployment Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Deployment Technology</em>'.
     * @see de.fhdo.ddmm.operation.DeploymentTechnologyReference#getDeploymentTechnology()
     * @see #getDeploymentTechnologyReference()
     * @generated
     */
    EReference getDeploymentTechnologyReference_DeploymentTechnology();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.DeploymentTechnologyReference#getContainer <em>Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Container</em>'.
     * @see de.fhdo.ddmm.operation.DeploymentTechnologyReference#getContainer()
     * @see #getDeploymentTechnologyReference()
     * @generated
     */
    EReference getDeploymentTechnologyReference_Container();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.InfrastructureNode <em>Infrastructure Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Infrastructure Node</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode
     * @generated
     */
    EClass getInfrastructureNode();

    /**
     * Returns the meta object for the reference list '{@link de.fhdo.ddmm.operation.InfrastructureNode#getUsedByNodes <em>Used By Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Used By Nodes</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode#getUsedByNodes()
     * @see #getInfrastructureNode()
     * @generated
     */
    EReference getInfrastructureNode_UsedByNodes();

    /**
     * Returns the meta object for the reference list '{@link de.fhdo.ddmm.operation.InfrastructureNode#getDependsOnNodes <em>Depends On Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Depends On Nodes</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode#getDependsOnNodes()
     * @see #getInfrastructureNode()
     * @generated
     */
    EReference getInfrastructureNode_DependsOnNodes();

    /**
     * Returns the meta object for the containment reference '{@link de.fhdo.ddmm.operation.InfrastructureNode#getInfrastructureTechnology <em>Infrastructure Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Infrastructure Technology</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode#getInfrastructureTechnology()
     * @see #getInfrastructureNode()
     * @generated
     */
    EReference getInfrastructureNode_InfrastructureTechnology();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.InfrastructureNode#getEndpoints <em>Endpoints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Endpoints</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode#getEndpoints()
     * @see #getInfrastructureNode()
     * @generated
     */
    EReference getInfrastructureNode_Endpoints();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.InfrastructureNode#getOperationModel <em>Operation Model</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Operation Model</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureNode#getOperationModel()
     * @see #getInfrastructureNode()
     * @generated
     */
    EReference getInfrastructureNode_OperationModel();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.InfrastructureTechnologyReference <em>Infrastructure Technology Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Infrastructure Technology Reference</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureTechnologyReference
     * @generated
     */
    EClass getInfrastructureTechnologyReference();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getImport <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Import</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getImport()
     * @see #getInfrastructureTechnologyReference()
     * @generated
     */
    EReference getInfrastructureTechnologyReference_Import();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getInfrastructureTechnology <em>Infrastructure Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Infrastructure Technology</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getInfrastructureTechnology()
     * @see #getInfrastructureTechnologyReference()
     * @generated
     */
    EReference getInfrastructureTechnologyReference_InfrastructureTechnology();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getInfrastructureNode <em>Infrastructure Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Infrastructure Node</em>'.
     * @see de.fhdo.ddmm.operation.InfrastructureTechnologyReference#getInfrastructureNode()
     * @see #getInfrastructureTechnologyReference()
     * @generated
     */
    EReference getInfrastructureTechnologyReference_InfrastructureNode();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.ImportedMicroservice <em>Imported Microservice</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Imported Microservice</em>'.
     * @see de.fhdo.ddmm.operation.ImportedMicroservice
     * @generated
     */
    EClass getImportedMicroservice();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ImportedMicroservice#getImport <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Import</em>'.
     * @see de.fhdo.ddmm.operation.ImportedMicroservice#getImport()
     * @see #getImportedMicroservice()
     * @generated
     */
    EReference getImportedMicroservice_Import();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ImportedMicroservice#getMicroservice <em>Microservice</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Microservice</em>'.
     * @see de.fhdo.ddmm.operation.ImportedMicroservice#getMicroservice()
     * @see #getImportedMicroservice()
     * @generated
     */
    EReference getImportedMicroservice_Microservice();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.ImportedMicroservice#getOperationNode <em>Operation Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Operation Node</em>'.
     * @see de.fhdo.ddmm.operation.ImportedMicroservice#getOperationNode()
     * @see #getImportedMicroservice()
     * @generated
     */
    EReference getImportedMicroservice_OperationNode();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification <em>Service Deployment Specification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Service Deployment Specification</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification
     * @generated
     */
    EClass getServiceDeploymentSpecification();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getImport <em>Import</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Import</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getImport()
     * @see #getServiceDeploymentSpecification()
     * @generated
     */
    EReference getServiceDeploymentSpecification_Import();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getService <em>Service</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Service</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getService()
     * @see #getServiceDeploymentSpecification()
     * @generated
     */
    EReference getServiceDeploymentSpecification_Service();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getServicePropertyValues <em>Service Property Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Service Property Values</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getServicePropertyValues()
     * @see #getServiceDeploymentSpecification()
     * @generated
     */
    EReference getServiceDeploymentSpecification_ServicePropertyValues();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getBasicEndpoints <em>Basic Endpoints</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Basic Endpoints</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getBasicEndpoints()
     * @see #getServiceDeploymentSpecification()
     * @generated
     */
    EReference getServiceDeploymentSpecification_BasicEndpoints();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getOperationNode <em>Operation Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Operation Node</em>'.
     * @see de.fhdo.ddmm.operation.ServiceDeploymentSpecification#getOperationNode()
     * @see #getServiceDeploymentSpecification()
     * @generated
     */
    EReference getServiceDeploymentSpecification_OperationNode();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.ProtocolAndDataFormat <em>Protocol And Data Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Protocol And Data Format</em>'.
     * @see de.fhdo.ddmm.operation.ProtocolAndDataFormat
     * @generated
     */
    EClass getProtocolAndDataFormat();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ProtocolAndDataFormat#getTechnology <em>Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Technology</em>'.
     * @see de.fhdo.ddmm.operation.ProtocolAndDataFormat#getTechnology()
     * @see #getProtocolAndDataFormat()
     * @generated
     */
    EReference getProtocolAndDataFormat_Technology();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ProtocolAndDataFormat#getProtocol <em>Protocol</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Protocol</em>'.
     * @see de.fhdo.ddmm.operation.ProtocolAndDataFormat#getProtocol()
     * @see #getProtocolAndDataFormat()
     * @generated
     */
    EReference getProtocolAndDataFormat_Protocol();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ProtocolAndDataFormat#getDataFormat <em>Data Format</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Data Format</em>'.
     * @see de.fhdo.ddmm.operation.ProtocolAndDataFormat#getDataFormat()
     * @see #getProtocolAndDataFormat()
     * @generated
     */
    EReference getProtocolAndDataFormat_DataFormat();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.ProtocolAndDataFormat#getEndpoint <em>Endpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Endpoint</em>'.
     * @see de.fhdo.ddmm.operation.ProtocolAndDataFormat#getEndpoint()
     * @see #getProtocolAndDataFormat()
     * @generated
     */
    EReference getProtocolAndDataFormat_Endpoint();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.BasicEndpoint <em>Basic Endpoint</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Basic Endpoint</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint
     * @generated
     */
    EClass getBasicEndpoint();

    /**
     * Returns the meta object for the attribute list '{@link de.fhdo.ddmm.operation.BasicEndpoint#getAddresses <em>Addresses</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Addresses</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint#getAddresses()
     * @see #getBasicEndpoint()
     * @generated
     */
    EAttribute getBasicEndpoint_Addresses();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.BasicEndpoint#getProtocols <em>Protocols</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Protocols</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint#getProtocols()
     * @see #getBasicEndpoint()
     * @generated
     */
    EReference getBasicEndpoint_Protocols();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.BasicEndpoint#getContainer <em>Container</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Container</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint#getContainer()
     * @see #getBasicEndpoint()
     * @generated
     */
    EReference getBasicEndpoint_Container();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.BasicEndpoint#getInfrastructureNode <em>Infrastructure Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Infrastructure Node</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint#getInfrastructureNode()
     * @see #getBasicEndpoint()
     * @generated
     */
    EReference getBasicEndpoint_InfrastructureNode();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.BasicEndpoint#getDeploymentSpecification <em>Deployment Specification</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Deployment Specification</em>'.
     * @see de.fhdo.ddmm.operation.BasicEndpoint#getDeploymentSpecification()
     * @see #getBasicEndpoint()
     * @generated
     */
    EReference getBasicEndpoint_DeploymentSpecification();

    /**
     * Returns the meta object for class '{@link de.fhdo.ddmm.operation.ImportedOperationAspect <em>Imported Operation Aspect</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Imported Operation Aspect</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect
     * @generated
     */
    EClass getImportedOperationAspect();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ImportedOperationAspect#getTechnology <em>Technology</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Technology</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect#getTechnology()
     * @see #getImportedOperationAspect()
     * @generated
     */
    EReference getImportedOperationAspect_Technology();

    /**
     * Returns the meta object for the reference '{@link de.fhdo.ddmm.operation.ImportedOperationAspect#getAspect <em>Aspect</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Aspect</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect#getAspect()
     * @see #getImportedOperationAspect()
     * @generated
     */
    EReference getImportedOperationAspect_Aspect();

    /**
     * Returns the meta object for the containment reference '{@link de.fhdo.ddmm.operation.ImportedOperationAspect#getSinglePropertyValue <em>Single Property Value</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Single Property Value</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect#getSinglePropertyValue()
     * @see #getImportedOperationAspect()
     * @generated
     */
    EReference getImportedOperationAspect_SinglePropertyValue();

    /**
     * Returns the meta object for the containment reference list '{@link de.fhdo.ddmm.operation.ImportedOperationAspect#getValues <em>Values</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Values</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect#getValues()
     * @see #getImportedOperationAspect()
     * @generated
     */
    EReference getImportedOperationAspect_Values();

    /**
     * Returns the meta object for the container reference '{@link de.fhdo.ddmm.operation.ImportedOperationAspect#getOperationNode <em>Operation Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the container reference '<em>Operation Node</em>'.
     * @see de.fhdo.ddmm.operation.ImportedOperationAspect#getOperationNode()
     * @see #getImportedOperationAspect()
     * @generated
     */
    EReference getImportedOperationAspect_OperationNode();

    /**
     * Returns the meta object for enum '{@link de.fhdo.ddmm.operation.ImportType <em>Import Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Import Type</em>'.
     * @see de.fhdo.ddmm.operation.ImportType
     * @generated
     */
    EEnum getImportType();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    OperationFactory getOperationFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each operation of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.OperationModelImpl <em>Model</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.OperationModelImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getOperationModel()
         * @generated
         */
        EClass OPERATION_MODEL = eINSTANCE.getOperationModel();

        /**
         * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_MODEL__IMPORTS = eINSTANCE.getOperationModel_Imports();

        /**
         * The meta object literal for the '<em><b>Containers</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_MODEL__CONTAINERS = eINSTANCE.getOperationModel_Containers();

        /**
         * The meta object literal for the '<em><b>Infrastructure Nodes</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_MODEL__INFRASTRUCTURE_NODES = eINSTANCE.getOperationModel_InfrastructureNodes();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.OperationNodeImpl <em>Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.OperationNodeImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getOperationNode()
         * @generated
         */
        EClass OPERATION_NODE = eINSTANCE.getOperationNode();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute OPERATION_NODE__NAME = eINSTANCE.getOperationNode_Name();

        /**
         * The meta object literal for the '<em><b>Technologies</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__TECHNOLOGIES = eINSTANCE.getOperationNode_Technologies();

        /**
         * The meta object literal for the '<em><b>Operation Environment</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__OPERATION_ENVIRONMENT = eINSTANCE.getOperationNode_OperationEnvironment();

        /**
         * The meta object literal for the '<em><b>Deployed Services</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__DEPLOYED_SERVICES = eINSTANCE.getOperationNode_DeployedServices();

        /**
         * The meta object literal for the '<em><b>Default Service Property Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__DEFAULT_SERVICE_PROPERTY_VALUES = eINSTANCE.getOperationNode_DefaultServicePropertyValues();

        /**
         * The meta object literal for the '<em><b>Deployment Specifications</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__DEPLOYMENT_SPECIFICATIONS = eINSTANCE.getOperationNode_DeploymentSpecifications();

        /**
         * The meta object literal for the '<em><b>Aspects</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference OPERATION_NODE__ASPECTS = eINSTANCE.getOperationNode_Aspects();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.ContainerImpl <em>Container</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.ContainerImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getContainer()
         * @generated
         */
        EClass CONTAINER = eINSTANCE.getContainer();

        /**
         * The meta object literal for the '<em><b>Deployment Technology</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTAINER__DEPLOYMENT_TECHNOLOGY = eINSTANCE.getContainer_DeploymentTechnology();

        /**
         * The meta object literal for the '<em><b>Default Basic Endpoints</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTAINER__DEFAULT_BASIC_ENDPOINTS = eINSTANCE.getContainer_DefaultBasicEndpoints();

        /**
         * The meta object literal for the '<em><b>Operation Model</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference CONTAINER__OPERATION_MODEL = eINSTANCE.getContainer_OperationModel();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.DeploymentTechnologyReferenceImpl <em>Deployment Technology Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.DeploymentTechnologyReferenceImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getDeploymentTechnologyReference()
         * @generated
         */
        EClass DEPLOYMENT_TECHNOLOGY_REFERENCE = eINSTANCE.getDeploymentTechnologyReference();

        /**
         * The meta object literal for the '<em><b>Import</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DEPLOYMENT_TECHNOLOGY_REFERENCE__IMPORT = eINSTANCE.getDeploymentTechnologyReference_Import();

        /**
         * The meta object literal for the '<em><b>Deployment Technology</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DEPLOYMENT_TECHNOLOGY_REFERENCE__DEPLOYMENT_TECHNOLOGY = eINSTANCE.getDeploymentTechnologyReference_DeploymentTechnology();

        /**
         * The meta object literal for the '<em><b>Container</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference DEPLOYMENT_TECHNOLOGY_REFERENCE__CONTAINER = eINSTANCE.getDeploymentTechnologyReference_Container();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.InfrastructureNodeImpl <em>Infrastructure Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.InfrastructureNodeImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getInfrastructureNode()
         * @generated
         */
        EClass INFRASTRUCTURE_NODE = eINSTANCE.getInfrastructureNode();

        /**
         * The meta object literal for the '<em><b>Used By Nodes</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_NODE__USED_BY_NODES = eINSTANCE.getInfrastructureNode_UsedByNodes();

        /**
         * The meta object literal for the '<em><b>Depends On Nodes</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_NODE__DEPENDS_ON_NODES = eINSTANCE.getInfrastructureNode_DependsOnNodes();

        /**
         * The meta object literal for the '<em><b>Infrastructure Technology</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_NODE__INFRASTRUCTURE_TECHNOLOGY = eINSTANCE.getInfrastructureNode_InfrastructureTechnology();

        /**
         * The meta object literal for the '<em><b>Endpoints</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_NODE__ENDPOINTS = eINSTANCE.getInfrastructureNode_Endpoints();

        /**
         * The meta object literal for the '<em><b>Operation Model</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_NODE__OPERATION_MODEL = eINSTANCE.getInfrastructureNode_OperationModel();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.InfrastructureTechnologyReferenceImpl <em>Infrastructure Technology Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.InfrastructureTechnologyReferenceImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getInfrastructureTechnologyReference()
         * @generated
         */
        EClass INFRASTRUCTURE_TECHNOLOGY_REFERENCE = eINSTANCE.getInfrastructureTechnologyReference();

        /**
         * The meta object literal for the '<em><b>Import</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_TECHNOLOGY_REFERENCE__IMPORT = eINSTANCE.getInfrastructureTechnologyReference_Import();

        /**
         * The meta object literal for the '<em><b>Infrastructure Technology</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_TECHNOLOGY_REFERENCE__INFRASTRUCTURE_TECHNOLOGY = eINSTANCE.getInfrastructureTechnologyReference_InfrastructureTechnology();

        /**
         * The meta object literal for the '<em><b>Infrastructure Node</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference INFRASTRUCTURE_TECHNOLOGY_REFERENCE__INFRASTRUCTURE_NODE = eINSTANCE.getInfrastructureTechnologyReference_InfrastructureNode();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.ImportedMicroserviceImpl <em>Imported Microservice</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.ImportedMicroserviceImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportedMicroservice()
         * @generated
         */
        EClass IMPORTED_MICROSERVICE = eINSTANCE.getImportedMicroservice();

        /**
         * The meta object literal for the '<em><b>Import</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_MICROSERVICE__IMPORT = eINSTANCE.getImportedMicroservice_Import();

        /**
         * The meta object literal for the '<em><b>Microservice</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_MICROSERVICE__MICROSERVICE = eINSTANCE.getImportedMicroservice_Microservice();

        /**
         * The meta object literal for the '<em><b>Operation Node</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_MICROSERVICE__OPERATION_NODE = eINSTANCE.getImportedMicroservice_OperationNode();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.ServiceDeploymentSpecificationImpl <em>Service Deployment Specification</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.ServiceDeploymentSpecificationImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getServiceDeploymentSpecification()
         * @generated
         */
        EClass SERVICE_DEPLOYMENT_SPECIFICATION = eINSTANCE.getServiceDeploymentSpecification();

        /**
         * The meta object literal for the '<em><b>Import</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_DEPLOYMENT_SPECIFICATION__IMPORT = eINSTANCE.getServiceDeploymentSpecification_Import();

        /**
         * The meta object literal for the '<em><b>Service</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_DEPLOYMENT_SPECIFICATION__SERVICE = eINSTANCE.getServiceDeploymentSpecification_Service();

        /**
         * The meta object literal for the '<em><b>Service Property Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_DEPLOYMENT_SPECIFICATION__SERVICE_PROPERTY_VALUES = eINSTANCE.getServiceDeploymentSpecification_ServicePropertyValues();

        /**
         * The meta object literal for the '<em><b>Basic Endpoints</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_DEPLOYMENT_SPECIFICATION__BASIC_ENDPOINTS = eINSTANCE.getServiceDeploymentSpecification_BasicEndpoints();

        /**
         * The meta object literal for the '<em><b>Operation Node</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference SERVICE_DEPLOYMENT_SPECIFICATION__OPERATION_NODE = eINSTANCE.getServiceDeploymentSpecification_OperationNode();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.ProtocolAndDataFormatImpl <em>Protocol And Data Format</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.ProtocolAndDataFormatImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getProtocolAndDataFormat()
         * @generated
         */
        EClass PROTOCOL_AND_DATA_FORMAT = eINSTANCE.getProtocolAndDataFormat();

        /**
         * The meta object literal for the '<em><b>Technology</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROTOCOL_AND_DATA_FORMAT__TECHNOLOGY = eINSTANCE.getProtocolAndDataFormat_Technology();

        /**
         * The meta object literal for the '<em><b>Protocol</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROTOCOL_AND_DATA_FORMAT__PROTOCOL = eINSTANCE.getProtocolAndDataFormat_Protocol();

        /**
         * The meta object literal for the '<em><b>Data Format</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROTOCOL_AND_DATA_FORMAT__DATA_FORMAT = eINSTANCE.getProtocolAndDataFormat_DataFormat();

        /**
         * The meta object literal for the '<em><b>Endpoint</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PROTOCOL_AND_DATA_FORMAT__ENDPOINT = eINSTANCE.getProtocolAndDataFormat_Endpoint();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.BasicEndpointImpl <em>Basic Endpoint</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.BasicEndpointImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getBasicEndpoint()
         * @generated
         */
        EClass BASIC_ENDPOINT = eINSTANCE.getBasicEndpoint();

        /**
         * The meta object literal for the '<em><b>Addresses</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute BASIC_ENDPOINT__ADDRESSES = eINSTANCE.getBasicEndpoint_Addresses();

        /**
         * The meta object literal for the '<em><b>Protocols</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_ENDPOINT__PROTOCOLS = eINSTANCE.getBasicEndpoint_Protocols();

        /**
         * The meta object literal for the '<em><b>Container</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_ENDPOINT__CONTAINER = eINSTANCE.getBasicEndpoint_Container();

        /**
         * The meta object literal for the '<em><b>Infrastructure Node</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_ENDPOINT__INFRASTRUCTURE_NODE = eINSTANCE.getBasicEndpoint_InfrastructureNode();

        /**
         * The meta object literal for the '<em><b>Deployment Specification</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference BASIC_ENDPOINT__DEPLOYMENT_SPECIFICATION = eINSTANCE.getBasicEndpoint_DeploymentSpecification();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl <em>Imported Operation Aspect</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportedOperationAspect()
         * @generated
         */
        EClass IMPORTED_OPERATION_ASPECT = eINSTANCE.getImportedOperationAspect();

        /**
         * The meta object literal for the '<em><b>Technology</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_OPERATION_ASPECT__TECHNOLOGY = eINSTANCE.getImportedOperationAspect_Technology();

        /**
         * The meta object literal for the '<em><b>Aspect</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_OPERATION_ASPECT__ASPECT = eINSTANCE.getImportedOperationAspect_Aspect();

        /**
         * The meta object literal for the '<em><b>Single Property Value</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE = eINSTANCE.getImportedOperationAspect_SinglePropertyValue();

        /**
         * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_OPERATION_ASPECT__VALUES = eINSTANCE.getImportedOperationAspect_Values();

        /**
         * The meta object literal for the '<em><b>Operation Node</b></em>' container reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference IMPORTED_OPERATION_ASPECT__OPERATION_NODE = eINSTANCE.getImportedOperationAspect_OperationNode();

        /**
         * The meta object literal for the '{@link de.fhdo.ddmm.operation.ImportType <em>Import Type</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see de.fhdo.ddmm.operation.ImportType
         * @see de.fhdo.ddmm.operation.impl.OperationPackageImpl#getImportType()
         * @generated
         */
        EEnum IMPORT_TYPE = eINSTANCE.getImportType();

    }

} //OperationPackage
