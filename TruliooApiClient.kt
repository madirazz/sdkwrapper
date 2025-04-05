package com.example.trulioosdkwrapper

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Client for interacting with Trulioo Customer API to generate shortcodes
 */
class TruliooApiClient {
    private val client = OkHttpClient()

    // Base URL for Trulioo Customer API (replace with actual URL from documentation)
    private val baseUrl = "https://api.trulioo.com/verification/v1"

    /**
     * Generate a shortcode using the Trulioo Customer API
     *
     * @param apiKey Your Trulioo API key
     * @param documentTypes List of document types to capture
     * @param callback Callback to receive the generated shortcode or error
     */
    suspend fun generateShortcode(
        apiKey: String,
        documentTypes: List<String> = listOf("PASSPORT", "DRIVERS_LICENSE", "ID_CARD"),
        callback: ShortcodeCallback
    ) = withContext(Dispatchers.IO) {
        try {
            // Create request body
            val jsonObject = JSONObject().apply {
                put("callbackUrl", "") // Optional callback URL
                put("configurationName", "DocumentVerification") // Replace with your config

                // Add document types
                val docTypesArray = JSONObject()
                documentTypes.forEach { docType ->
                    docTypesArray.put(docType, true)
                }
                put("documentTypes", docTypesArray)
            }

            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = jsonObject.toString().toRequestBody(mediaType)

            // Create request
            val request = Request.Builder()
                .url("$baseUrl/shortcodes")
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer $apiKey")
                .build()

            // Execute request
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    callback.onError("API error: ${response.code} - ${response.message}")
                    return@withContext
                }

                val responseBody = response.body?.string() ?: ""
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val shortcode = jsonResponse.optString("shortcode", "")
                    if (shortcode.isNotEmpty()) {
                        callback.onSuccess(shortcode)
                    } else {
                        callback.onError("No shortcode in response: $responseBody")
                    }
                } catch (e: Exception) {
                    callback.onError("Failed to parse response: ${e.message}")
                }
            }
        } catch (e: IOException) {
            callback.onError("Network error: ${e.message}")
        } catch (e: Exception) {
            callback.onError("Error: ${e.message}")
        }
    }

    // Simple callback interface for shortcode generation
    interface ShortcodeCallback {
        fun onSuccess(shortcode: String)
        fun onError(message: String)
    }
}

// Usage example in Activity:
/*
lifecycleScope.launch {
    TruliooApiClient().generateShortcode(
        apiKey = BuildConfig.TRULIOO_API_KEY,
        object : TruliooApiClient.ShortcodeCallback {
            override fun onSuccess(shortcode: String) {
                this@MainActivity.shortCode = shortcode
                startTruliooWorkflow()
            }

            override fun onError(message: String) {
                // Handle error
                Toast.makeText(this@MainActivity, "Error: $message", Toast.LENGTH_LONG).show()
            }
        }
    )
}
*/