# Changelog

## [2.3.20-0.0.6] - 2025-11-05
### Enhance
+ Add tracker initialization check
+ Add placeholder tracker url to prevent error when creating the retrofit

## [2.3.20-0.0.5] - 2025-11-05
### Enhance
+ New versioning convention
+ FEAT: Add Hilt qualifiers for Gson and ConverterFactory

## [2.3.20-4] - 2025-11-04
### Enhance
+ FEAT: Remove unnecessary variables from init function and expose that variable to set from outside Tracker

## [2.3.20-3] - 2025-11-04
### Enhance
+ FEAT: Takeout TrackerConfig.kt, because actually controller can be directly injected inside class

## [2.3.20-2] - 2025-11-04
### Enhance
+ REFACTOR: Migrate DI from Koin to Hilt and improve configuration handling
+ CHORE: Update dependencies
+ FEAT: Add Hilt dependencies
+ FEAT: Upgrade Gradle and Android build tools

## [2.3.20-1] - 2025-11-03
### Enhance
+ Add initialization check for tracker functions for logging only 
+ Remove tracker initialization on every method