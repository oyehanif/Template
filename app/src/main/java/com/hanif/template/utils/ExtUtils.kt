package com.hanif.template.utils

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


/* Created by Hanif on 29/08/24 */

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Modifier.removeSpace(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp,
): Modifier = this.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    fun Dp.toPxInt(): Int = this.toPx().toInt()

    layout(
        placeable.width - (horizontal * 2).toPxInt(),
        placeable.height - (vertical * 2).toPxInt()
    ) {
        placeable.placeRelative(-horizontal.toPx().toInt(), -vertical.toPx().toInt())
    }
}

fun Modifier.applyGradientOrColor(
    colors: List<Int>,
    startPoint: Offset = Offset(0f, 0f),
    endPoint: Offset = Offset(1f, 1f)
): Modifier {
    return if (colors.size == 1) {
        this.background(color = Color(colors.first()))
    } else {
        var size = androidx.compose.ui.unit.IntSize.Zero
        val color = colors.map { Color(it) }
        this
            .onGloballyPositioned { coordinates: LayoutCoordinates ->
                size = coordinates.size
            }
            .background(
                brush = Brush.linearGradient(
                    colors = color,
                    start = startPoint,
                    end = Offset(
                        x = endPoint.x * size.width,
                        y = endPoint.y * size.height
                    )
                )
            )
    }
}


fun String.toColor(): Int {
    val hex = this.trim().removePrefix("#")
    val colorInt = android.graphics.Color.parseColor("#$hex")
    return colorInt
}


fun Context.shareDialog(){
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Daylet")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    this.startActivity(shareIntent)
}


//fun String.logd(tag: String = "MiLogger") {
//    if (BuildConfig.DEBUG) Log.d(tag, this)
//}
//
//fun String.loge(tag: String = "MiLogger") {
//    if (BuildConfig.DEBUG) Log.e(tag, this)
//}
//
//fun String.logdv(tag: String = "MiLogger") {
//    if (BuildConfig.DEBUG) Log.v(tag, this)
//}
//
//fun String.logi(tag: String = "MiLogger") {
//    if (BuildConfig.DEBUG) Log.i(tag, this)
//}
//
//fun String.logw(tag: String = "MiLogger") {
//    if (BuildConfig.DEBUG) Log.w(tag, this)
//}

// Extension function to convert ISO 8601 string to a human-readable format
fun String.toReadableDate(): String {
    // Define the ISO 8601 date format
    val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    isoFormat.timeZone = TimeZone.getTimeZone("UTC")  // Set UTC time zone

    // Parse the ISO 8601 string into a Date object
    val date: Date = isoFormat.parse(this) ?: return "Invalid Date"

    // Define the desired output format (e.g., "September 02, 2024, 12:00 AM")
    val outputFormat = SimpleDateFormat("MMMM dd, yyyy, hh:mm a", Locale.getDefault())

    // Format the Date object into a human-readable string
    return outputFormat.format(date)
}