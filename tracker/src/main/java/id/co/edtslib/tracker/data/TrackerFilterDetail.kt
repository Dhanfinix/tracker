package id.co.edtslib.tracker.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class TrackerFilterDetail (
    @field:SerializedName("filter_type")
    val filterType: String,
    @field:SerializedName("element")
    val element: String,
    @field:SerializedName("filter")
    val filter: List<Any>
)