package org.mozilla.tv.firefox.fathom

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import mozilla.components.concept.engine.EngineView
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.ext.addJavascriptInterface
import org.mozilla.tv.firefox.ext.runFathomScript

class Fathom(private val context: Context) {

    private var engineView: EngineView? = null
    //private var sessionIsLoadingObserver: SessionIsLoadingObserver? = null

    val script = context.resources.openRawResource(R.raw.extract).use { it.bufferedReader().readText() }

    fun onCreateEngineView(engineView: EngineView) {
        this.engineView = engineView.apply {
            addJavascriptInterface(FathomObject(), "java")
        }

//        val sessionIsLoadingObserver = SessionIsLoadingObserver(engineView, session)
//        session.register(sessionIsLoadingObserver, owner = context)
//        this.sessionIsLoadingObserver = sessionIsLoadingObserver
    }

//    inner class SessionIsLoadingObserver(private val engineView: EngineView, private val session: Session) : Session.Observer {
//        override fun onLoadingStateChanged(session: Session, loading: Boolean) {
//            if (!loading) {
//                // TODO only inject once
//                    engineView.runFathomScript(script)
//            }
//        }
//    }

    inner class FathomObject {
        @JavascriptInterface
        fun setTitle(title: String) {
            Log.d("michelle", title)
        }
    }

    fun runFathomScript() {
        engineView?.runFathomScript(script)
    }

}
