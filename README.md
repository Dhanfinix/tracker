# Tracker

![SlidingButton](https://i.ibb.co/GCcGMwH/edtslibs.png)

A lightweight, framework-agnostic analytics library for Android with **no DI framework dependencies**. Works seamlessly with any project architecture.

## Features

✅ **Zero Dependencies** - No DI framework required  
✅ **Universal Compatibility** - Works with Hilt, Dagger, Koin, or no DI at all  
✅ **Simple Setup** - Initialize once, use everywhere  
✅ **Lifecycle Safe** - Built-in lifecycle awareness  
✅ **Secure Storage** - Automatic encrypted SharedPreferences

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

**Maven:**
```xml
<dependency>
  <groupId>id.co.edtslib</groupId>
  <artifactId>tracker</artifactId>
  <version>2.3.21-0.0.13</version>
</dependency>
```

## Usage

### 1. Initialize in Application Class

Create an Application class and initialize Tracker in `onCreate()`:

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Tracker
        Tracker.init(
            app = this,
            baseUrl = "https://your-api.com/tracker/",
            token = "your-api-token",
            path = "apps-tracker-gateway",  // optional, default: "apps-tracker-gateway"
            isLegacy = false                 // optional, default: false
        )
        
        // Optional: Configure additional settings
        Tracker.debugging = BuildConfig.DEBUG
        Tracker.resend = true
        Tracker.appVersion = BuildConfig.VERSION_NAME
    }
}
```

Register your Application class in `AndroidManifest.xml`:
```xml
<application
    android:name=".App"
    ...>
</application>
```

### 2. Get Tracker Instance

Access the tracker singleton anywhere in your app:

```kotlin
import id.co.edtslib.tracker.di.manual.TrackerFactory

class MainActivity : AppCompatActivity() {
    
    private val tracker by lazy { TrackerFactory.getTracker() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Track page view
        tracker.trackPage("Home", "home_screen")
        
        // Track user action
        tracker.trackClick("login_button", category = "authentication")
    }
    
    override fun onResume() {
        super.onResume()
        tracker.resumePage("Home", "home_screen")
    }
}
```

## API Reference

### Configuration

```kotlin
// Set user identifier
tracker.setUserId(userId: Long)

// Set user location
tracker.setLatLng(lat: Double, lng: Double)

// Set service identifier
tracker.setService(service: String)
```

### Page Tracking

```kotlin
// Track page view
tracker.trackPage(
    pageName: String,
    pageId: String,
    pageUrlPath: String = ""
)

// Track page details
tracker.trackPageDetail(detail: Any?)

// Resume page (for onResume lifecycle)
tracker.resumePage(pageName: String, pageId: String)
```

### Event Tracking

```kotlin
// Track user clicks
tracker.trackClick(
    name: String,
    category: String? = null,
    url: String? = null,
    details: Any? = null
)

// Track search
tracker.trackSearch(
    keyword: String,
    details: Any? = null
)

// Track filters applied
tracker.trackFilters(
    filters: List<TrackerFilterDetail>,
    category: String = ""
)

// Track sort action
tracker.trackSort(sortType: String)
```

### Impression Tracking

```kotlin
// Track impression manually
tracker.trackImpression<SourceType, MappedType>(
    category: String,
    data: List<*>,
    mapper: ((data: SourceType) -> MappedType)? = null
)

// Auto-track RecyclerView impressions
tracker.setImpressionRecyclerView<SourceType, MappedType>(
    category: String,
    recyclerView: RecyclerView,
    mapper: ((data: SourceType) -> MappedType)? = null
)

// Track displayed items
tracker.trackDisplayedItems(data: MutableList<Any>)
```

### Form Submission Tracking

```kotlin
// Track successful submission
tracker.trackSubmissionSuccess(
    name: String,
    category: String,
    details: Any? = null
)

// Track failed submission
tracker.trackSubmissionFailed(
    name: String,
    category: String,
    reason: String?,
    details: Any? = null
)
```

### Application Lifecycle

```kotlin
// Track app lifecycle events
tracker.trackOpenApplication()
tracker.trackCloseApplication()
tracker.trackResumeApplication()
tracker.trackMinimizeApplication()
```

### Install Attribution

```kotlin
// Check install referrer (Google Play)
tracker.checkInstallReferrer(activity: FragmentActivity)

// Or manually provide referrer data
tracker.checkInstallReferrer(
    utm_raw: String?,
    intent: Intent?
)

// Get install referrer data
tracker.getInstallReferer()
```

### Data Access

```kotlin
// Get current tracker data
val data: TrackerData? = tracker.getData()

// Get previous page name (useful for navigation context)
val priorPageName: String? = tracker.getPriorPageName()
```

## Advanced Configuration

### Debug Mode

Enable detailed logging during development:

```kotlin
Tracker.debugging = true  // Enable logs
```

### Resend Failed Requests

Configure automatic retry for failed tracking requests:

```kotlin
Tracker.resend = true  // Enable automatic retry (default: true)
```

### Custom App Version

Override the app version for tracking:

```kotlin
Tracker.appVersion = "1.0.0-beta"
```

## Integration with DI Frameworks

### Works with Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideTracker(): Tracker {
        return TrackerFactory.getTracker()
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var tracker: Tracker
}
```

### Works with Koin

```kotlin
val appModule = module {
    single { TrackerFactory.getTracker() }
}

class MainActivity : AppCompatActivity() {
    private val tracker: Tracker by inject()
}
```

### Works with Dagger

```kotlin
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideTracker(): Tracker = TrackerFactory.getTracker()
}
```

### Works Without Any DI

```kotlin
class MainActivity : AppCompatActivity() {
    private val tracker = TrackerFactory.getTracker()
}
```

## Example Usage

### Complete Activity Example

```kotlin
class ProductListActivity : AppCompatActivity() {
    
    private val tracker by lazy { TrackerFactory.getTracker() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        
        // Track page view
        tracker.trackPage("Product List", "product_list")
        
        // Setup impression tracking for RecyclerView
        tracker.setImpressionRecyclerView<Product, ProductImpressionData>(
            category = "products",
            recyclerView = recyclerView,
            mapper = { product ->
                ProductImpressionData(
                    id = product.id,
                    name = product.name,
                    price = product.price
                )
            }
        )
        
        // Track filter application
        filterButton.setOnClickListener {
            tracker.trackFilters(
                filters = listOf(
                    TrackerFilterDetail("price", "100-500"),
                    TrackerFilterDetail("category", "electronics")
                ),
                category = "products"
            )
        }
        
        // Track sort
        sortButton.setOnClickListener {
            tracker.trackSort("price_low_to_high")
        }
    }
    
    override fun onResume() {
        super.onResume()
        tracker.resumePage("Product List", "product_list")
    }
}
```

## Migration from Hilt-based Version

If you're upgrading from a Hilt-based version of this library:

### Before (Hilt)
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var tracker: Tracker
}
```

### After (Manual DI)
```kotlin
class MainActivity : AppCompatActivity() {
    private val tracker by lazy { TrackerFactory.getTracker() }
}
```

**Changes Required:**
1. Remove `@HiltAndroidApp` from Application class (if only used for Tracker)
2. Remove Tracker Hilt modules
3. Add `Tracker.init()` call in `Application.onCreate()`
4. Replace injection with `TrackerFactory.getTracker()`

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

For issues, questions, or contributions, please visit [https://github.com/Dhanfinix/tracker]