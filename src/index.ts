import { registerPlugin } from '@capacitor/core'

import type { MockLocationPlugin } from './definitions'

/**
 * DewaKoding Capacitor Mock Location Plugin
 * 
 * Plugin to detect mock/fake GPS location on Android devices.
 * 
 * @example
 * ```typescript
 * import { MockLocation } from '@dewakoding/capacitor-mock-location'
 * 
 * const { isMock } = await MockLocation.checkMockLocation()
 * if (isMock) {
 *   console.log('Mock location detected!')
 * }
 * ```
 */
const MockLocation = registerPlugin<MockLocationPlugin>('MockLocation', {
  web: () => import('./web').then(m => new m.MockLocationWeb()),
})

export * from './definitions'
export { MockLocation }

