package com.example.trulioosdkwrapper

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.startup.AppInitializer
import com.example.trulioosdkwrapper.databinding.ActivityMainBinding
import com.trulioo.docv.TruliooResult
import com.trulioo.docv.TruliooWorkflow
import com.trulioo.docv.Trulioo
import com.trulioo.docv.core.registry.RegistryInitializer
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var shortCode: String = "sample-short-code" // Will be updated with generated code

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the CoreRegistry manually if needed
        try {
            AppInitializer.getInstance(this)
                .initializeComponent(RegistryInitializer::class.java)
        } catch (e: Exception) {
            // Handle initialization error
            Toast.makeText(this, "Error initializing Trulioo: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Set up button click listener
        binding.btnStartVerification.setOnClickListener {
            // Generate shortcode or use a static one
            generateShortcodeAndStartWorkflow()
        }
    }

    private fun generateShortcodeAndStartWorkflow() {
        // Show loading state
        binding.btnStartVerification.isEnabled = false
        binding.btnStartVerification.text = "Generating shortcode..."

        // Option 1: Use API to generate shortcode
        lifecycleScope.launch {
            try {
                TruliooApiClient().generateShortcode(
                    apiKey = BuildConfig.TRULIOO_API_KEY,
                    documentTypes = listOf("PASSPORT", "DRIVERS_LICENSE", "ID_CARD"),
                    callback = object : TruliooApiClient.ShortcodeCallback {
                        override fun onSuccess(shortcode: String) {
                            this@MainActivity.shortCode = shortcode
                            startTruliooWorkflow()
                        }

                        override fun onError(message: String) {
                            // Handle error
                            Toast.makeText(this@MainActivity, "Error: $message", Toast.LENGTH_LONG).show()

                            // Reset button
                            binding.btnStartVerification.isEnabled = true
                            binding.btnStartVerification.text = "Start Verification"

                            // Option 2: Use static shortcode as fallback
                            // If you have a valid static shortcode, you could use it here
                            // this@MainActivity.shortCode = "your-static-shortcode"
                            // startTruliooWorkflow()
                        }
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                binding.btnStartVerification.isEnabled = true
                binding.btnStartVerification.text = "Start Verification"
            }
        }
    }

    private fun startTruliooWorkflow() {
        val workflow = TruliooWorkflow(shortCode)

        lifecycleScope.launch {
            try {
                val activityLauncher = Trulioo().registerForActivityResult(this@MainActivity, workflow) { result ->
                    // Reset button state
                    binding.btnStartVerification.isEnabled = true
                    binding.btnStartVerification.text = "Start Verification"

                    when (result) {
                        is TruliooResult.Complete -> {
                            // Handle successful verification
                            val message = "Verification completed successfully\nTransaction ID: ${result.transactionId}"
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
                            binding.tvResult.text = message
                        }
                        is TruliooResult.Error -> {
                            // Handle verification error
                            val message = "Verification error: ${result.code}\n${result.message}"
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            binding.tvResult.text = message
                        }
                        is TruliooResult.Exception -> {
                            // Handle exception
                            val message = "Verification exception: ${result.message}"
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                            binding.tvResult.text = message
                        }
                    }
                }

                if (activityLauncher is com.trulioo.docv.common.Result.Success) {
                    activityLauncher.value.launch(workflow)
                } else {
                    binding.btnStartVerification.isEnabled = true
                    binding.btnStartVerification.text = "Start Verification"
                    Toast.makeText(this, "Failed to start verification", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.btnStartVerification.isEnabled = true
                binding.btnStartVerification.text = "Start Verification"
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}