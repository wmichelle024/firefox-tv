package org.mozilla.tv.firefox.fathom

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.annotation.UiThread
import mozilla.components.concept.engine.EngineView
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.ChannelListAdapter
import org.mozilla.tv.firefox.ext.addJavascriptInterface
import org.mozilla.tv.firefox.ext.runFathomScript

class Fathom(
        private val context: Context,
        private val channelListAdapter: ChannelListAdapter
) {

    private var engineView: EngineView? = null
    //private var sessionIsLoadingObserver: SessionIsLoadingObserver? = null

    val script = context.resources.openRawResource(R.raw.extract).use { it.bufferedReader().readText() }

    fun onCreateEngineView(engineView: EngineView) {
        this.engineView = engineView.apply {
            addJavascriptInterface(FathomObject(), "java")
        }

    }

    inner class FathomObject {
        @JavascriptInterface
        @UiThread
        fun setTitle(title: String) {
            if (title.toFloat() > 0.5) {
                channelListAdapter.onCreateDialog(R.string.music_channel_title)
            } else {
                channelListAdapter.onCreateDialog()
            }
        }
    }

    fun runFathomScript() {
        engineView?.runFathomScript(script)
    }

}
