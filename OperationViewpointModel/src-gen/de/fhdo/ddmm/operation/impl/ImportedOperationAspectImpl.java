/**
 */
package de.fhdo.ddmm.operation.impl;

import de.fhdo.ddmm.data.PrimitiveValue;

import de.fhdo.ddmm.operation.ImportedOperationAspect;
import de.fhdo.ddmm.operation.OperationNode;
import de.fhdo.ddmm.operation.OperationPackage;

import de.fhdo.ddmm.technology.OperationAspect;
import de.fhdo.ddmm.technology.TechnologySpecificPropertyValueAssignment;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Imported Operation Aspect</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl#getImportedAspect <em>Imported Aspect</em>}</li>
 *   <li>{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl#getSinglePropertyValue <em>Single Property Value</em>}</li>
 *   <li>{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl#getValues <em>Values</em>}</li>
 *   <li>{@link de.fhdo.ddmm.operation.impl.ImportedOperationAspectImpl#getOperationNode <em>Operation Node</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ImportedOperationAspectImpl extends MinimalEObjectImpl.Container implements ImportedOperationAspect {
    /**
     * The cached value of the '{@link #getImportedAspect() <em>Imported Aspect</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getImportedAspect()
     * @generated
     * @ordered
     */
    protected OperationAspect importedAspect;

    /**
     * The cached value of the '{@link #getSinglePropertyValue() <em>Single Property Value</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSinglePropertyValue()
     * @generated
     * @ordered
     */
    protected PrimitiveValue singlePropertyValue;

    /**
     * The cached value of the '{@link #getValues() <em>Values</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getValues()
     * @generated
     * @ordered
     */
    protected EList<TechnologySpecificPropertyValueAssignment> values;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ImportedOperationAspectImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return OperationPackage.Literals.IMPORTED_OPERATION_ASPECT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationAspect getImportedAspect() {
        if (importedAspect != null && importedAspect.eIsProxy()) {
            InternalEObject oldImportedAspect = (InternalEObject)importedAspect;
            importedAspect = (OperationAspect)eResolveProxy(oldImportedAspect);
            if (importedAspect != oldImportedAspect) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT, oldImportedAspect, importedAspect));
            }
        }
        return importedAspect;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationAspect basicGetImportedAspect() {
        return importedAspect;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setImportedAspect(OperationAspect newImportedAspect) {
        OperationAspect oldImportedAspect = importedAspect;
        importedAspect = newImportedAspect;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT, oldImportedAspect, importedAspect));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PrimitiveValue getSinglePropertyValue() {
        return singlePropertyValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetSinglePropertyValue(PrimitiveValue newSinglePropertyValue, NotificationChain msgs) {
        PrimitiveValue oldSinglePropertyValue = singlePropertyValue;
        singlePropertyValue = newSinglePropertyValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE, oldSinglePropertyValue, newSinglePropertyValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSinglePropertyValue(PrimitiveValue newSinglePropertyValue) {
        if (newSinglePropertyValue != singlePropertyValue) {
            NotificationChain msgs = null;
            if (singlePropertyValue != null)
                msgs = ((InternalEObject)singlePropertyValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE, null, msgs);
            if (newSinglePropertyValue != null)
                msgs = ((InternalEObject)newSinglePropertyValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE, null, msgs);
            msgs = basicSetSinglePropertyValue(newSinglePropertyValue, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE, newSinglePropertyValue, newSinglePropertyValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<TechnologySpecificPropertyValueAssignment> getValues() {
        if (values == null) {
            values = new EObjectContainmentEList<TechnologySpecificPropertyValueAssignment>(TechnologySpecificPropertyValueAssignment.class, this, OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES);
        }
        return values;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationNode getOperationNode() {
        if (eContainerFeatureID() != OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE) return null;
        return (OperationNode)eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OperationNode basicGetOperationNode() {
        if (eContainerFeatureID() != OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE) return null;
        return (OperationNode)eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetOperationNode(OperationNode newOperationNode, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject)newOperationNode, OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationNode(OperationNode newOperationNode) {
        if (newOperationNode != eInternalContainer() || (eContainerFeatureID() != OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE && newOperationNode != null)) {
            if (EcoreUtil.isAncestor(this, newOperationNode))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newOperationNode != null)
                msgs = ((InternalEObject)newOperationNode).eInverseAdd(this, OperationPackage.OPERATION_NODE__ASPECTS, OperationNode.class, msgs);
            msgs = basicSetOperationNode(newOperationNode, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE, newOperationNode, newOperationNode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetOperationNode((OperationNode)otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE:
                return basicSetSinglePropertyValue(null, msgs);
            case OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES:
                return ((InternalEList<?>)getValues()).basicRemove(otherEnd, msgs);
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                return basicSetOperationNode(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                return eInternalContainer().eInverseRemove(this, OperationPackage.OPERATION_NODE__ASPECTS, OperationNode.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT:
                if (resolve) return getImportedAspect();
                return basicGetImportedAspect();
            case OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE:
                return getSinglePropertyValue();
            case OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES:
                return getValues();
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                if (resolve) return getOperationNode();
                return basicGetOperationNode();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT:
                setImportedAspect((OperationAspect)newValue);
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE:
                setSinglePropertyValue((PrimitiveValue)newValue);
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES:
                getValues().clear();
                getValues().addAll((Collection<? extends TechnologySpecificPropertyValueAssignment>)newValue);
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                setOperationNode((OperationNode)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT:
                setImportedAspect((OperationAspect)null);
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE:
                setSinglePropertyValue((PrimitiveValue)null);
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES:
                getValues().clear();
                return;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                setOperationNode((OperationNode)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case OperationPackage.IMPORTED_OPERATION_ASPECT__IMPORTED_ASPECT:
                return importedAspect != null;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__SINGLE_PROPERTY_VALUE:
                return singlePropertyValue != null;
            case OperationPackage.IMPORTED_OPERATION_ASPECT__VALUES:
                return values != null && !values.isEmpty();
            case OperationPackage.IMPORTED_OPERATION_ASPECT__OPERATION_NODE:
                return basicGetOperationNode() != null;
        }
        return super.eIsSet(featureID);
    }

} //ImportedOperationAspectImpl