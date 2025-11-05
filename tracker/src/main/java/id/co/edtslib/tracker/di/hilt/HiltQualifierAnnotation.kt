package id.co.edtslib.tracker.di.hilt

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TrackerOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TrackerRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TrackerSharePref

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TrackerGson

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class TrackerConverterFactory