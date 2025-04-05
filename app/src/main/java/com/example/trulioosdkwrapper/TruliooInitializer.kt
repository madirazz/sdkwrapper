package com.example.trulioosdkwrapper

import android.content.Context
import androidx.startup.Initializer
import com.trulioo.docv.core.registry.RegistryInitializer
import java.util.Collections

/**
 * An initializer that ensures the Trulioo SDK is properly initialized
 * at application startup.
 */
class TruliooInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        // Initialize Trulioo SDK dependencies
        // You can add any custom initialization logic here if needed
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // Make sure the RegistryInitializer runs before this initializer
        return Collections.singletonList(RegistryInitializer::class.java)
    }
}