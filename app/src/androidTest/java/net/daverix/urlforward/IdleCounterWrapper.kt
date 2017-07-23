package net.daverix.urlforward

import android.support.test.espresso.idling.CountingIdlingResource


class IdleCounterWrapper(val idlingCounter: CountingIdlingResource) : IdleCounter {
    override fun increment() {
        idlingCounter.increment()
    }

    override fun decrement() {
        idlingCounter.decrement()
    }
}
