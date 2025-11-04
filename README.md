# Tracker

![SlidingButton](https://i.ibb.co/GCcGMwH/edtslibs.png)
## Setup
### Gradle

Add this to your project level `build.gradle`:
```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
Add this to your app `build.gradle`:
```groovy
dependencies {
    implementation 'com.github.edtslib:tracker:latest'
}
```

### Usage with Hilt
1. Application Setup
   Create an Application class and annotate it with @HiltAndroidApp. Register it in your AndroidManifest.xml:

```kotlin
@HiltAndroidApp
class App : Application()
```
```xml
<application
android:name=".App">
</application>
```

2. Provide TrackerConfig via Hilt
   In your app module, create a Hilt module to supply runtime configuration:
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object TrackerInitModule {

    @Provides
    @Singleton
    fun provideTrackerConfig(): TrackerConfig {
        return TrackerConfig(
            baseUrl = "https://your-api.com/tracker/",
            token = "your-api-token",
            path = "apps-tracker-gateway",
            isLegacy = false,
            resend = true,
            debugging = BuildConfig.DEBUG,
            appVersion = BuildConfig.VERSION_NAME
        )
    }
}
```
3. Inject and Use Tracker
   Once configured, you can inject Tracker into any Hilt-aware class:
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var tracker: Tracker

    override fun onResume() {
        super.onResume()
        tracker.resumePage()
    }
}
```

### Methods
Here is all static tracker method, call as Tracker.<mehtod_name>

```kotlin

fun setUserId(userId: Long)

fun trackPage(screenName: String)

fun trackPageDetail(name: String, detail: Any?)

fun trackClick(name: String)

fun trackFilters(name: String, filters: List<String>)

fun trackSort(name: String, sortType: String)

fun trackImpression(name: String, data: Any)

fun trackSubmissionSuccess(name: String)

fun trackSubmissionFailed(name: String, reason: String?)

fun trackExitApplication()

fun checkInstallReferrer(activity: FragmentActivity)

fun checkInstallReferrer(utm_raw: String?, intent: Intent?)

```
