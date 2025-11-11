package id.co.edtslib.tracker.util

import android.util.Log
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.serializer

fun List<*>.toSafeJsonElement(): List<JsonElement> {
    return this.mapNotNull { it.toSafeAnyJsonElement() }
}

/**
 * For arbitrary objects without @Serializable annotation
 */
fun Any?.toSafeAnyJsonElement(): JsonElement? = when (this) {
    null -> JsonNull
    is JsonElement -> this
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    is Map<*, *> -> {
        val safeMap = this.entries.associate {
            it.key.toString() to (it.value.toSafeAnyJsonElement() ?: JsonNull)
        }
        JsonObject(safeMap)
    }
    is List<*> -> {
        JsonArray(this.mapNotNull { it.toSafeAnyJsonElement() })
    }
    else -> {
        // Try to serialize as @Serializable class
        try {
            Json.encodeToJsonElement(this)
        } catch (e: SerializationException) {
            Log.e("TrackerSerializationUtil", "Cannot serialize $this: ${e.message}")
            null
        }
    }
}

/** For @Serializable data classes */
inline fun <reified T> T?.toSafeJsonElement(): JsonElement? {
    if (this == null) return JsonNull
//TODO
// Tracker impression failed to encode with this error
// Unexpected error: java.lang.NullPointerException: Parameter specified as non-null is null: method kotlinx.serialization.SerializersKt__SerializersKt.serializer, parameter type
    return try {
        val serializer = serializer<T>()
        Json.encodeToJsonElement(serializer,this)
    } catch (e: SerializationException) {
        Log.e("TrackerSerializationUtil", "Serialization failed for ${T::class.simpleName}: ${e.message}")
        (this as? Any)?.toSafeAnyJsonElement()
    } catch (e: Exception) {
        Log.e("TrackerSerializationUtil", "Unexpected error: $e")
        null
    }
}
