package com.duckduckgo.voice.impl

import android.app.Activity
import androidx.activity.result.ActivityResultCaller
import com.duckduckgo.app.statistics.pixels.Pixel
import com.duckduckgo.di.scopes.ActivityScope
import com.duckduckgo.voice.api.VoiceSearchLauncher.Event
import com.duckduckgo.voice.api.VoiceSearchLauncher.Source
import com.duckduckgo.voice.impl.ActivityResultLauncherWrapper.Action.LaunchVoiceSearch
import com.duckduckgo.voice.impl.ActivityResultLauncherWrapper.Request
import com.duckduckgo.voice.impl.listeningmode.ui.VoiceSearchBackgroundBlurRenderer
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

interface VoiceSearchActivityLauncher {
    fun registerResultsCallback(
        caller: ActivityResultCaller,
        activity: Activity,
        source: Source,
        onEvent: (Event) -> Unit
    )

    fun launch(activity: Activity)
}

@ContributesBinding(ActivityScope::class)
class RealVoiceSearchActivityLauncher @Inject constructor(
    private val blurRenderer: VoiceSearchBackgroundBlurRenderer,
    private val pixel: Pixel,
    private val activityResultLauncherWrapper: ActivityResultLauncherWrapper
) : VoiceSearchActivityLauncher {

    companion object {
        private const val KEY_PARAM_SOURCE = "source"
    }

    private lateinit var _source: Source

    override fun registerResultsCallback(
        caller: ActivityResultCaller,
        activity: Activity,
        source: Source,
        onEvent: (Event) -> Unit
    ) {
        _source = source
        activityResultLauncherWrapper.register(
            caller,
            Request.ResultFromVoiceSearch { code, data ->
                if (code == Activity.RESULT_OK) {
                    if (data.isNotEmpty()) {
                        pixel.fire(
                            pixel = VoiceSearchPixelNames.VOICE_SEARCH_DONE,
                            parameters = mapOf(KEY_PARAM_SOURCE to _source.paramValueName)
                        )
                        onEvent(Event.VoiceRecognitionSuccess(data))
                    } else {
                        onEvent(Event.SearchCancelled)
                    }
                } else {
                    onEvent(Event.SearchCancelled)
                }

                activity.window?.decorView?.rootView?.let {
                    blurRenderer.removeBlur(it)
                }
            }
        )
    }

    override fun launch(activity: Activity) {
        launchVoiceSearch(activity)
    }

    private fun launchVoiceSearch(activity: Activity) {
        activity.window?.decorView?.rootView?.let {
            blurRenderer.addBlur(it)
        }
        pixel.fire(
            pixel = VoiceSearchPixelNames.VOICE_SEARCH_STARTED,
            parameters = mapOf(KEY_PARAM_SOURCE to _source.paramValueName)
        )
        activityResultLauncherWrapper.launch(LaunchVoiceSearch)
    }
}
