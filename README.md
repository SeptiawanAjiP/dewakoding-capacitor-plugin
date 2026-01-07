# @dewakoding/capacitor-mock-location

Capacitor plugin to detect mock or fake GPS locations on Android devices.

## Overview

This plugin helps you detect when users are using fake GPS applications to spoof their location. It's useful for apps that require accurate location data, such as attendance systems, location-based services, or tracking applications.

## Installation

```bash
npm install @dewakoding/capacitor-mock-location
```

or with yarn:

```bash
yarn add @dewakoding/capacitor-mock-location
```

Then sync Capacitor:

```bash
npx cap sync
```

## Usage

```typescript
import { MockLocation } from '@dewakoding/capacitor-mock-location'

const { isMock } = await MockLocation.checkMockLocation()

if (isMock) {
  console.log('Mock location detected')
  // Handle mock location case
} else {
  console.log('Location is valid')
  // Proceed with location-based operations
}
```

### Example: Check before check-in

```typescript
import { MockLocation } from '@dewakoding/capacitor-mock-location'
import { Geolocation } from '@capacitor/geolocation'

async function handleCheckIn() {
  // Check for mock location first
  const { isMock } = await MockLocation.checkMockLocation()
  
  if (isMock) {
    alert('Fake GPS detected. Please disable fake GPS apps.')
    return
  }
  
  // Get actual location
  const position = await Geolocation.getCurrentPosition()
  
  // Continue with check-in process
}
```

## API

### checkMockLocation()

Checks if the device is currently using a mock or fake GPS location.

**Returns:** `Promise<{ isMock: boolean }>`

- `isMock`: `true` if mock location is detected, `false` otherwise

## How It Works

The plugin uses multiple detection methods based on standard Android APIs:

1. Checks if the current location is from a mock provider using `isFromMockProvider()` (Android 6.0+)
2. Scans for providers containing "mock" or "test" keywords in LocationManager
3. Checks if common fake GPS applications are installed on the device

**Note:** This implementation uses publicly available Android APIs. The code structure and implementation approach are original work by DewaKoding.

## Android Setup

### Permissions

Make sure your `AndroidManifest.xml` includes location permissions:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Auto-Registration

The plugin uses `@CapacitorPlugin` annotation, so it will be auto-registered by Capacitor. No manual registration in MainActivity is required.

### Build & Run

```bash
npm run cap:sync
npm run cap:run:android
```

## Platform Support

- Android: Fully supported
- iOS: Not yet implemented (returns `false`)
- Web: Not applicable (returns `false`)

## Testing

### Test with Fake GPS App

1. Install a fake GPS app (e.g., "Fake GPS Location" from Play Store)
2. Enable mock location in the fake GPS app
3. Set the desired location
4. Open your app
5. Call `checkMockLocation()`
6. Should return `{ isMock: true }`

### Test with Real Location

1. Make sure no fake GPS apps are active
2. Open your app
3. Call `checkMockLocation()`
4. Should return `{ isMock: false }`

## Troubleshooting

### Plugin not detected

1. Make sure you've run `npx cap sync`
2. Rebuild the Android app
3. Check logcat for errors
4. Verify the package name in the Java file is correct

### Build errors

1. Make sure the Java file uses the correct package: `com.dewakoding.capacitor.mocklocation`
2. Check if there are duplicate plugins with the same name
3. Make sure Capacitor version is compatible (^8.0.0)

### Mock location not detected

1. Some sophisticated fake GPS apps may not be detected
2. Make sure the fake GPS app is active and being used
3. Try restarting the app after enabling fake GPS

## Limitations

Detection is not 100% accurate. Some sophisticated fake GPS applications may not be detected. The plugin checks for common fake GPS apps, but new applications may not be in the detection list.

## Development

Build the plugin:

```bash
npm run build
```

Watch mode:

```bash
npm run watch
```

## License

MIT License