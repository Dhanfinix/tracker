package id.co.edtslib.tracker.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import id.co.edtslib.tracker.Tracker

/**
 * A customizable button composable with an optional click tracker.
 *
 * This composable is a wrapper around Material 3's `Button` that adds a feature to automatically
 * track click events using a tracker utility.
 */
@Composable
fun TrackerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enableClickTracker: Boolean = false,
    trackerTitle: String? = null,
    trackerCategory: String? = null,
    trackerUrl: String? = null,
    trackerDetails: Any? = null,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = {
            if (enableClickTracker){
                Tracker.trackClick(
                    trackerTitle.orEmpty(),
                    trackerCategory,
                    trackerUrl,
                    trackerDetails
                )
            }
            onClick()
        },
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}