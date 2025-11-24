# Tracker

![SlidingButton](https://i.ibb.co/GCcGMwH/edtslibs.png)
-----

A lightweight, thread-safe, and framework-agnostic analytics library for Android. Built with a robust **Builder Pattern** and **Manual Dependency Injection**, ensuring zero conflicts with your host app's architecture.

## Features

✅ **Zero Transitive Dependencies** - Does not force Koin, Hilt, or Dagger on your app  
✅ **Thread Safe** - Synchronized singleton initialization and localized state management  
✅ **Memory Safe** - Automatic handling of `ApplicationContext` and `WeakReferences` for Views  
✅ **Crash Resistant** - "Loud Logging" instead of runtime crashes if initialized incorrectly  
✅ **Lifecycle Aware** - Self-cleaning observers and lifecycle-safe context handling  
✅ **Secure Storage** - Automatic encrypted SharedPreferences with legacy fallback

## Setup

### Gradle

Ask author for `github.properties` file

```properties
USER_ID=AUTHOR_USER_ID
ACCESS_TOKEN=AUTHOR_ACCESS_TOKEN
```

Add this to your project level `settings.gradle`:

```groovy
val githubPropertiesFile = File(rootDir, "github.properties")
val githubProperties = java.util.Properties()

if (githubPropertiesFile.exists()) {
    githubProperties.load(githubPropertiesFile.inputStream())
}

maven {
    name = "GitHubPackages"
    url = uri("https://maven.pkg.github.com/Dhanfinix/tracker")
    credentials {
        username = githubProperties["USER_ID"] as String? ?: System.getenv("USER_ID")
        password = githubProperties["ACCESS_TOKEN"] as String? ?: System.getenv("ACCESS_TOKEN")
    }
}
```

Add this to your app `build.gradle`:

```groovy
dependencies {
    implementation 'id.co.edtslib:tracker:2.3.21-0.0.13'
}
```

## Usage

### 1\. Initialize in Application Class

You **must** initialize the Tracker using the `Builder` in your `Application.onCreate()`.

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 1. Build the Configuration
        Tracker.Builder(this)
            .setBaseUrl("https://your-api.com/tracker/")
            .setToken("your-api-token")
            .setPath("apps-tracker-gateway") // Optional
            .setDebugging(BuildConfig.DEBUG) // Enable logs in Debug mode
            .setResend(true)                 // Retry failed requests
            .setAppVersion(BuildConfig.VERSION_NAME)
            .setSingleton(true)              // Sets this as the global instance (Default: true)
            .build()
            
        // No need to assign it to a variable if setSingleton(true) is used.
    }
}
```

### 2\. Track Events

Access the tracker via the static Companion methods anywhere in your app. The library handles the singleton instance internally.

```kotlin
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Track page view
        Tracker.trackPage("Home", "home_screen")
        
        // Track user action
        Tracker.trackClick("login_button", category = "authentication")
    }
    
    override fun onResume() {
        super.onResume()
        Tracker.resumePage("Home", "home_screen")
    }
}
```

## API Reference

### Configuration & User

```kotlin
// Set user identifier
Tracker.setUserId(userId: Long)

// Set user location
Tracker.setLatLng(lat: Double, lng: Double)

// Set service identifier
Tracker.setService(service: String)
```

### Page Tracking

```kotlin
// Track page view
Tracker.trackPage(
    pageName: String,
    pageId: String,
    pageUrlPath: String = ""
)

// Track page details
Tracker.trackPageDetail(detail: Any?)

// Resume page (for onResume lifecycle)
Tracker.resumePage(pageName: String, pageId: String)
```

### Event Tracking

```kotlin
// Track user clicks
Tracker.trackClick(
    name: String,
    category: String? = null,
    url: String? = null,
    details: Any? = null
)

// Track search
Tracker.trackSearch(
    keyword: String,
    details: Any? = null
)

// Track filters applied
Tracker.trackFilters(
    filters: List<TrackerFilterDetail>,
    category: String = ""
)

// Track sort action
Tracker.trackSort(sortType: String)
```

### Impression Tracking

```kotlin
// Track impression manually
Tracker.trackImpression<SourceType, MappedType>(
    category: String,
    data: List<*>,
    mapper: ((data: SourceType) -> MappedType)? = null
)

// Auto-track RecyclerView impressions
// NOTE: Automatically handles scroll state per RecyclerView instance
Tracker.setImpressionRecyclerView<SourceType, MappedType>(
    category: String,
    recyclerView: RecyclerView,
    mapper: ((data: SourceType) -> MappedType)? = null
)

// Track displayed items
Tracker.trackDisplayedItems(data: MutableList<Any>)
```

### Form Submission Tracking

```kotlin
// Track successful submission
Tracker.trackSubmissionSuccess(
    name: String,
    category: String,
    details: Any? = null
)

// Track failed submission
Tracker.trackSubmissionFailed(
    name: String,
    category: String,
    reason: String?,
    details: Any? = null
)
```

### Application Lifecycle

```kotlin
// Track app lifecycle events
Tracker.trackOpenApplication()
Tracker.trackCloseApplication()
Tracker.trackResumeApplication()
Tracker.trackMinimizeApplication()
```

### Install Attribution

```kotlin
// Check install referrer (Google Play)
Tracker.checkInstallReferrer(activity: FragmentActivity)

// Or manually provide referrer data
Tracker.checkInstallReferrer(
    utm_raw: String?,
    intent: Intent?
)

// Get install referrer data
Tracker.getInstallReferer()
```

### Data Access

```kotlin
// Get current tracker data
val data: TrackerData? = Tracker.getData()

// Get previous page name (useful for navigation context)
val priorPageName: String? = Tracker.getPriorPageName()

// Get local config (if needed)
val config = Tracker.getTrackerLocalConfig()
```

## Integration with DI Frameworks

Since this library uses a **Manual DI** approach internally, it is compatible with **all** DI frameworks without requiring any specific modules.

### Works with Hilt / Dagger

You do not need to provide a `@Module`. Simply call the static methods directly.

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // No @Inject needed!
    
    fun onUserAction() {
        Tracker.trackClick("btn_save")
    }
}
```

### Works with Koin

You do not need to declare a module.

```kotlin
class MainActivity : AppCompatActivity() {
    // No 'by inject()' needed!
    
    fun onUserAction() {
        Tracker.trackClick("btn_save")
    }
}
```

## Unit Testing

Because the library uses a Singleton pattern by default, testing requires careful management of state. You can initialize a fresh instance for testing without setting it as a singleton if needed, or simply build a new singleton for every test case.

```kotlin
@Test
fun testTracking() {
    // Re-initialize for test environment
    Tracker.Builder(context)
        .setBaseUrl("http://localhost:8080")
        .setSingleton(true)
        .build()
        
    Tracker.trackClick("test_click")
}
```

## Requirements

- Android SDK 21+ (Android 5.0 Lollipop)
- Kotlin 1.5+

## License

**Internal Use Only**

This library is proprietary software developed by [EDTS] for internal use.

- ✅ Free to use within [EDTS]
- ❌ Not licensed for external use
- ❌ No redistribution permitted

## Support

For issues, questions, or contributions, please visit [[https://github.com/Dhanfinix/tracker](https://github.com/Dhanfinix/tracker)]