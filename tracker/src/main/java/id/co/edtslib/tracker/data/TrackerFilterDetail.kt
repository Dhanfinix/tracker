package id.co.edtslib.tracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class TrackerFilterDetail (
    @SerialName("filter_type")
    val filterType: String,
    @SerialName("element")
    val element: String,
    @SerialName("filter")
    val filter: List<JsonElement>
)