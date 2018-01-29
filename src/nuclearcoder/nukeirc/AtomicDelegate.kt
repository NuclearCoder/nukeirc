package nuclearcoder.nukeirc

import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KProperty

/**
 * Created by NuclearCoder on 2018-01-26.
 */

operator fun <T> AtomicReference<T>.getValue(thisRef: Any?,
                                             property: KProperty<*>): T {
    return get() ?: error("Reference isn't initialized")
}

operator fun <T> AtomicReference<T>.setValue(thisRef: Any?,
                                             property: KProperty<*>,
                                             value: T) {
    set(value)
}
