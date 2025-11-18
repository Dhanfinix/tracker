# Changelog

## [2.3.21-0.0.13] - 2025-11-18
### FEAT
+ Add priorPageName which returns actual previous page name for usage outside of track page

## [2.3.20-0.0.12] - 2025-11-06
### FEAT
+ Add tracker composable for Button and LazyColumn

## [2.3.20-0.0.11] - 2025-11-06
### FEAT
+ Include sourcesJar and JavadocJar

## [2.3.20-0.0.10] - 2025-11-06
### CHANGE
+ Configure Maven publication from release components containing dependencies metadata

## [2.3.20-0.0.9] - 2025-11-06
### FEAT
+ FEAT: Change dependencies from implementation to api.

## [2.3.20-0.0.8] - 2025-11-05
### FEAT
+ Add Hilt util for manual dependency access

## [2.3.20-0.0.7] - 2025-11-05
### FEAT
+ Add Hilt entry point for tracker

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