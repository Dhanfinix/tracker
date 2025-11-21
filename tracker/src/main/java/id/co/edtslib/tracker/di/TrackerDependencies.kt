package id.co.edtslib.tracker.di

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.securepreferences.SecurePreferences
import id.co.edtslib.tracker.BuildConfig
import id.co.edtslib.tracker.Tracker
import id.co.edtslib.tracker.data.TrackerApiService
import id.co.edtslib.tracker.data.TrackerRemoteDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class TrackerDependencies(
    private val context: Context,
    private val config: Tracker.TrackerConfig
) {
    private val trackerSharedPref: SharedPreferences by lazy {
        getSharedPrefs()
    }
    private val trackerRetrofit: Retrofit by lazy {
        getRetrofit()
    }
    private val trackerApiService: TrackerApiService by lazy {
        trackerRetrofit.create(TrackerApiService::class.java)
    }
    private val trackerRemoteDataSource: TrackerRemoteDataSource by lazy {
        TrackerRemoteDataSource(trackerApiService, config)
    }
    private val trackerLocalDataSource: TrackerLocalDataSource by lazy {
        TrackerLocalDataSource(trackerSharedPref, context, config)
    }
    val configurationLocalSource: ConfigurationLocalSource by lazy {
        ConfigurationLocalSource(trackerSharedPref)
    }
    private val trackerRepository: ITrackerRepository by lazy {
        TrackerRepository(
            trackerRemoteDataSource,
            trackerLocalDataSource,
            configurationLocalSource
        )
    }
    private val trackerUseCase: TrackerUseCase by lazy {
        TrackerInteractor(trackerRepository)
    }
    val controller: TrackerController by lazy {
        TrackerController(trackerUseCase)
    }

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(config.baseUrl)
        .client(
            UnsafeOkHttpClient(config).get()
                .newBuilder()
                .addInterceptor(AuthInterceptor(config.token, config.isLegacy))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    private fun getSharedPrefs(): SharedPreferences {
        return try {
            if (config.debugging) {
                PreferenceManager.getDefaultSharedPreferences(context)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val spec = KeyGenParameterSpec.Builder(
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                    .build()

                val masterKey = MasterKey.Builder(context)
                    .setKeyGenParameterSpec(spec)
                    .build()

                EncryptedSharedPreferences.create(
                    context,
                    "edts_tracker_secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } else {
                SecurePreferences(
                    context,
                    BuildConfig.DB_PASS,
                    "edts_tracker_secret_shared_prefs"
                )
            }
        } catch (e: Exception) {
            PreferenceManager.getDefaultSharedPreferences(context)
        } catch (e: NoClassDefFoundError) {
            PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}