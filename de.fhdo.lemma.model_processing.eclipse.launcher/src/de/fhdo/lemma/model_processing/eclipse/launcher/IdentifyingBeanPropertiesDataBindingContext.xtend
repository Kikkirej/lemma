package de.fhdo.lemma.model_processing.eclipse.launcher

import org.eclipse.core.databinding.DataBindingContext
import org.eclipse.core.databinding.Binding
import org.eclipse.core.databinding.observable.value.IObservableValue
import org.eclipse.core.databinding.UpdateValueStrategy
import org.eclipse.core.databinding.beans.typed.BeanProperties
import java.util.Objects
import java.util.function.Consumer

/*class IdentifyingBeanPropertiesDataBindingContext extends DataBindingContext {
    val IDENTIFIED_BINDINGS = <BindingIdentifier<?>, Binding>newHashMap
    val IDENTIFIED_MODELS = <BindingIdentifier<?>, IObservableValue<?>>newHashMap

    static class BindingIdentifier<S> {
        Class<S> beanClass
        String propertyName
        S source

        new(Class<S> beanClass, String propertyName, S source) {
            this.beanClass = beanClass
            this.propertyName = propertyName
            this.source = source
        }

        override equals(Object o) {
            return if (o === this)
                true
            else if (!(o instanceof BindingIdentifier))
                false
            else {
                val otherBinding = o as BindingIdentifier<?>
                beanClass == otherBinding.beanClass &&
                propertyName == otherBinding.propertyName &&
                source == otherBinding.source
            }
        }

        override hashCode() {
            return Objects.hash(beanClass.hashCode, propertyName.hashCode, source.hashCode)
        }
    }

    val IDENTIFIERS_PER_SOURCE = <Object, BindingIdentifier<?>>newHashMap*/
    //val Consumer<Binding> newBindingCallback
    //val Consumer<Binding> removedBindingCallback

    /*new(Consumer<Binding> newBindingCallback, Consumer<Binding> removedBindingCallback) {
        this.newBindingCallback = newBindingCallback
        this.removedBindingCallback = removedBindingCallback
    }*/

    /*final def <S, M> getModelForIdentifier(Class<S> beanClass, String propertyName,
        Class<M> valueType, S source) {
        val identifier = new BindingIdentifier(beanClass, propertyName, source)
        var model = IDENTIFIED_MODELS.get(identifier) as IObservableValue<M>
        if (model === null) {
            model = BeanProperties.value(beanClass, propertyName, valueType).observe(source)
            IDENTIFIED_MODELS.put(identifier, model)
        }

        return model
    }*/

    /*final def <S, M, T> Binding bindIfNotExists(IObservableValue<T> target, Class<S> beanClass,
        String propertyName, S source) {
        return bindIfNotExists(target, beanClass, propertyName, null, source, null, null)
    }*/

    /*final def <S, M, T> Binding bindIfNotExists(IObservableValue<T> target, Class<S> beanClass,
        String propertyName, Class<M> valueType, S source) {
        return bindIfNotExists(target, beanClass, propertyName, valueType, source, null, null)
    }

    final def <S, M, T> Binding bindIfNotExists(
        IObservableValue<T> target,
        Class<S> beanClass,
        String propertyName,
        Class<M> valueType,
        S source,
        UpdateValueStrategy<? super T, ? extends M> targetToModel,
        UpdateValueStrategy<? super M, ? extends T> modelToTarget
    ) {
        val identifier = new BindingIdentifier(beanClass, propertyName, source)
        val existingBinding = IDENTIFIED_BINDINGS.get(identifier)
        return if (existingBinding !== null)
                existingBinding
            else {
                val model = getModelForIdentifier(beanClass, propertyName, valueType, source)
                val newBinding = bindValue(target, model, targetToModel, modelToTarget)
                IDENTIFIED_BINDINGS.put(identifier, newBinding)
                IDENTIFIERS_PER_SOURCE.put(source, identifier)
                newBindingCallback.accept(newBinding)
                newBinding
            }
    }*/

    /*final def removeBindingOfSource(Object source) {
        val identifier = IDENTIFIERS_PER_SOURCE.remove(source)
        if (identifier === null) {
            return
        }

        IDENTIFIED_MODELS.remove(identifier)

        val removedBinding = IDENTIFIED_BINDINGS.remove(identifier)
        if (removedBinding !== null)
            removedBindingCallback.accept(removedBinding)
    }*/
//}