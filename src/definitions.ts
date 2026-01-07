/**
 * DewaKoding Capacitor Mock Location Plugin
 * Type definitions for the plugin interface
 */

export interface MockLocationPlugin {
  /**
   * Check if the device is using mock location
   * 
   * This method checks if the device is using mock/fake GPS location
   * using multiple detection methods:
   * 1. Check if location is from mock provider (Android 6.0+)
   * 2. Check for mock/test providers in LocationManager
   * 3. Check for common fake GPS apps installed on device
   * 
   * @returns Promise with isMock boolean indicating if mock location is detected
   * 
   * @example
   * ```typescript
   * import { MockLocation } from '@dewakoding/capacitor-mock-location'
   * 
   * const { isMock } = await MockLocation.checkMockLocation()
   * if (isMock) {
   *   console.log('Mock location detected!')
   *   // Block action or show warning
   * } else {
   *   console.log('Location is valid')
   *   // Proceed with location-based operations
   * }
   * ```
   */
  checkMockLocation(): Promise<{ isMock: boolean }>
}

