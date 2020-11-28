package pl.dziczyzna.common

import androidx.collection.ArraySet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

internal class SingleLiveData<T> : MediatorLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val observers = ArraySet<ObserverWrapper<in T>>()

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val observerWrapper = ObserverWrapper(observer)
        observers.add(observerWrapper)
        super.observe(owner, observerWrapper)
    }

    override fun observeForever(observer: Observer<in T>) {
        val observerWrapper = ObserverWrapper(observer)
        observers.add(observerWrapper)
        super.observeForever(observerWrapper)
    }

    override fun removeObserver(observer: Observer<in T>) {
        if (observers.remove<Observer<in T>>(observer)) {
            super.removeObserver(observer)
            return
        }

        observers.find { wrapper ->
            wrapper.observer == observer
        }?.let { wrapper ->
            observers.remove(wrapper)
            super.removeObserver(wrapper)
        }
    }

    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

    private inner class ObserverWrapper<T>(val observer: Observer<in T>) : Observer<T> {

        override fun onChanged(t: T) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }
}