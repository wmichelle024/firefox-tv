package org.mozilla.tv.firefox.fathom

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity
import mozilla.components.browser.session.Session
import mozilla.components.concept.engine.EngineView
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.ext.addJavascriptInterface
import org.mozilla.tv.firefox.ext.runFathomScript

class Fathom(private val activity: AppCompatActivity) {

    private var engineView: EngineView? = null
    private var sessionIsLoadingObserver: SessionIsLoadingObserver? = null

    val script = activity.resources.openRawResource(R.raw.extract).use { it.bufferedReader().readText() }

    fun onCreateEngineView(engineView: EngineView, session: Session) {
        this.engineView = engineView.apply {
            addJavascriptInterface(FathomObject(), "java")
        }

        val sessionIsLoadingObserver = SessionIsLoadingObserver(engineView, session)
        session.register(sessionIsLoadingObserver, owner = activity)
        this.sessionIsLoadingObserver = sessionIsLoadingObserver
    }

    inner class SessionIsLoadingObserver(private val engineView: EngineView, private val session: Session) : Session.Observer {
        override fun onLoadingStateChanged(session: Session, loading: Boolean) {
            if (!loading) {
                // TODO only inject once
                engineView.runFathomScript(script)
            }
        }
    }

    inner class FathomObject {
        @JavascriptInterface
        fun setTitle(title: String) {
            Log.d("michelle", title)
        }
    }
}
