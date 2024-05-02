package com.nitaioanmadalin.artviewer.core.utils.lifecycle

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifecycleHandler(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onAny: (() -> Unit)? = null,
) {
    val curOnCreate by rememberUpdatedState(newValue = onCreate)
    val curOnStart by rememberUpdatedState(newValue = onStart)
    val curOnResume by rememberUpdatedState(newValue = onResume)
    val curOnPause by rememberUpdatedState(newValue = onPause)
    val curOnStop by rememberUpdatedState(newValue = onStop)
    val curOnDestroy by rememberUpdatedState(newValue = onDestroy)
    val curOnAny by rememberUpdatedState(newValue = onAny)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            Log.d(TAG, event.name)
            when (event) {
                Lifecycle.Event.ON_CREATE -> curOnCreate?.invoke()
                Lifecycle.Event.ON_START -> curOnStart?.invoke()
                Lifecycle.Event.ON_RESUME -> curOnResume?.invoke()
                Lifecycle.Event.ON_PAUSE -> curOnPause?.invoke()
                Lifecycle.Event.ON_STOP -> curOnStop?.invoke()
                Lifecycle.Event.ON_DESTROY -> curOnDestroy?.invoke()
                Lifecycle.Event.ON_ANY -> curOnAny?.invoke()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

const val TAG = "LifecycleHandler"