package com.dewakoding.capacitor.mocklocation

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

/**
 * DewaKoding Capacitor Plugin - Mock Location Detection
 *
 * Plugin to detect mock/fake GPS location on Android devices.
 *
 * This plugin uses multiple detection methods:
 * 1. Check if location is from mock provider (Android 6.0+)
 * 2. Check for mock/test providers in LocationManager
 * 3. Check for common fake GPS apps installed on device
 *
 * @author DewaKoding
 * @version 1.0.0
 */
@CapacitorPlugin(name = "MockLocation")
class MockLocationPlugin : Plugin() {

    @PluginMethod
    fun checkMockLocation(call: PluginCall) {
        try {
            val isMock = isMockLocationEnabled()

            val ret = JSObject()
            ret.put("isMock", isMock)
            call.resolve(ret)
        } catch (e: Exception) {
            call.reject("Error checking mock location: ${e.message}", e)
        }
    }

    /**
     * Detects if mock location is enabled using multiple methods
     *
     * @return true if mock location is detected, false otherwise
     */
    private fun isMockLocationEnabled(): Boolean {
        val context = context

        // Method 1: Check if "Allow mock locations" is enabled in Developer Options
        // This only works for Android 5.1 and below
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION,
                0
            ) != 0
        } else {
            // For Android 6.0 and above, ALLOW_MOCK_LOCATION is deprecated
            // We need to use alternative methods
            return try {
                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    ?: return false

                // Method 2: Check if last known location is from mock provider
                val location = getLastKnownLocation(locationManager)
                if (location?.isFromMockProvider == true) {
                    return true
                }

                // Method 3: Check for mock/test providers
                if (hasMockProvider(locationManager)) {
                    return true
                }

                // Method 4: Check for common fake GPS apps
                if (hasFakeGpsAppInstalled(context)) {
                    return true
                }

                false
            } catch (e: Exception) {
                // If any error occurs, assume not mock (fail-safe)
                false
            }
        }
    }

    /**
     * Get last known location from GPS or Network provider
     */
    private fun getLastKnownLocation(locationManager: LocationManager): Location? {
        return try {
            var location: Location? = null
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (location == null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            location
        } catch (e: SecurityException) {
            // Location permission not granted
            null
        }
    }

    /**
     * Check if LocationManager has any mock or test providers
     */
    private fun hasMockProvider(locationManager: LocationManager): Boolean {
        return try {
            val providers = locationManager.allProviders
            providers.any { provider ->
                val lowerProvider = provider.lowercase()
                lowerProvider.contains("mock") || lowerProvider.contains("test")
            }
        } catch (e: Exception) {
            // Ignore errors
            false
        }
    }

    /**
     * Check if common fake GPS apps are installed on the device
     *
     * List of commonly used fake GPS applications
     */
    private fun hasFakeGpsAppInstalled(context: Context): Boolean {
        val mockLocationApps = arrayOf(
            "com.lexa.fakegps",
            "com.blogspot.newapphorizons.fakegps",
            "com.incorporateapps.fakegps.free",
            "com.androidsx.fakegps",
            "com.cygery.rejsekort.developer",
            "com.doctormobile.locationfake",
            "com.lerist.fakelocation",
            "com.github.android_apps.mock_location",
            "ru.gavrikov.mocklocations",
            "com.henrythompson.fakegps",
            "com.evezzon.fakegps",
            "com.blogspot.newapphorizons.fakegps",
            "com.incorporateapps.fakegps.free"
        )

        val pm = context.packageManager
        return mockLocationApps.any { packageName ->
            try {
                pm.getPackageInfo(packageName, 0)
                // If we can get package info, the app is installed
                true
            } catch (e: android.content.pm.PackageManager.NameNotFoundException) {
                // App not installed, continue
                false
            }
        }
    }
}

