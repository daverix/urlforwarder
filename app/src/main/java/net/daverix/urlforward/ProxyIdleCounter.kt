package net.daverix.urlforward

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProxyIdleCounter @Inject constructor() : IdleCounter {
    var idleResource: IdleCounter? = null

    override fun increment() {
        idleResource?.increment()
    }

    override fun decrement() {
        idleResource?.decrement()
    }
}
