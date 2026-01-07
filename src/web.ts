import { WebPlugin } from '@capacitor/core'

import type { MockLocationPlugin } from './definitions'

/**
 * Web implementation for DewaKoding Mock Location Plugin
 * 
 * Web platform doesn't support mock location detection,
 * so it always returns false (not mock)
 */
export class MockLocationWeb extends WebPlugin implements MockLocationPlugin {
  async checkMockLocation(): Promise<{ isMock: boolean }> {
    // Web platform doesn't support mock location detection
    // Return false as default (not mock)
    return { isMock: false }
  }
}

