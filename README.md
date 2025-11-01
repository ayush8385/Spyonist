# Spyonist

*Spyonist* is an Android privacy monitoring application that helps you stay aware of when apps access your device's camera and microphone. The app displays visual indicators on your screen whenever any third-party app attempts to use these sensitive sensors, providing real-time privacy alerts and comprehensive usage logs.

## Features

•⁠  ⁠*Camera & Microphone Monitoring*: Detects and alerts you when apps access your camera or microphone in real-time
•⁠  ⁠*Visual Indicators*: Shows customizable on-screen indicators (camera/microphone icons) when sensors are in use
•⁠  ⁠*Access Logs*: Maintains a detailed log of all camera and microphone access events, including timestamps and app names
•⁠  ⁠*Customizable Appearance*: 
  - Choose indicator colors (foreground and background)
  - Adjust indicator position (left, center, or right)
  - Toggle individual indicators on/off
•⁠  ⁠*App Whitelisting*: Configure which apps should trigger alerts and which should be excluded
•⁠  ⁠*Alert System*: Sound and vibration notifications when camera/microphone access is detected
•⁠  ⁠*Lock Screen Monitoring*: Optional monitoring even when device is locked
•⁠  ⁠*Privacy-First*: The app operates locally with no internet permissions and does not collect any user data

## How It Works

Spyonist uses Android's AccessibilityService to monitor system-level camera and microphone access. When an app requests access to these sensors, Spyonist:
1.⁠ ⁠Detects the access attempt
2.⁠ ⁠Displays a visual indicator overlay on your screen
3.⁠ ⁠Logs the event to a local database
4.⁠ ⁠Optionally triggers sound/vibration alerts

The app runs as a foreground service and uses AccessibilityService permissions (which you grant manually) to detect app switches and sensor usage without requiring dangerous permissions that could compromise your privacy.

## Technology Stack

•⁠  ⁠*Language*: Kotlin
•⁠  ⁠*Architecture*: MVVM (Model-View-ViewModel)
•⁠  ⁠*Database*: Room Database (for access logs)
•⁠  ⁠*UI*: Android Jetpack (ViewBinding, LiveData, ViewModel)
•⁠  ⁠*Services*: AccessibilityService for monitoring

## Privacy & Security

Spyonist is designed with privacy in mind:
•⁠  ⁠No internet access permissions
•⁠  ⁠All data stored locally on your device
•⁠  ⁠No data collection or transmission
•⁠  ⁠Open-source code for transparency

---

*Made in India 🇮🇳*
