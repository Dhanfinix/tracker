package id.co.edtslib.tracker.di.hilt

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.securepreferences.SecurePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.edtslib.tracker.BuildConfig
import id.co.edtslib.tracker.data.TrackerConfig
import java.lang.Exception
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Provides
    @Singleton
    @TrackerSharePref
    fun provideTrackerSharedPref(
        app: Application,
        config: TrackerConfig
    ): SharedPreferences =
        try {
            if (config.debugging) {
                PreferenceManager.getDefaultSharedPreferences(app)
            }
            else
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val spec = KeyGenParameterSpec.Builder(
                        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                        .build()
                    val masterKey = MasterKey.Builder(app)
                        .setKeyGenParameterSpec(spec)
                        .build()

                    EncryptedSharedPreferences.create(
                        app,
                        "edts_tracker_secret_shared_prefs",
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )
                } else {
                    SecurePreferences(
                        app,
                        BuildConfig.DB_PASS,
                        "edts_tracker_secret_shared_prefs"
                    )
                }
        }

        catch (e: Exception) {
            PreferenceManager.getDefaultSharedPreferences(app)
        }
        catch (e: NoClassDefFoundError) {
            PreferenceManager.getDefaultSharedPreferences(app)
        }
}