package org.mozilla.tv.firefox.channels

import android.app.Dialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.dialog_pin_site.*
import kotlinx.android.synthetic.main.dialog_pin_site.cancelButton
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.content.ChannelContentRepo
import org.mozilla.tv.firefox.channels.pinnedtile.CustomPinnedTile
import org.mozilla.tv.firefox.channels.pinnedtile.PinnedTileRepo
import org.mozilla.tv.firefox.session.SessionRepo
import java.util.*

class ChannelListAdapter(
        private val sessionRepo: SessionRepo,
        private val pinnedTileRepo: PinnedTileRepo,
        private val channelContentRepo: ChannelContentRepo
) {

    private lateinit var context: Context
    private lateinit var spinnerAdapter: ArrayAdapter<String>
    private lateinit var spinner: Spinner
    private lateinit var dialog: Dialog

    private val channelTitles = arrayOf(
            R.string.pinned_tile_channel_title,
            R.string.news_channel_title,
            R.string.sports_channel_title,
            R.string.music_channel_title
    )

    fun onCreate(context: Context) {
        this.context = context
    }

    fun onCreateDialog(title: Int = R.string.pinned_tile_channel_title) {
        dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_pin_site)
        spinnerAdapter =  ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, channelTitles.map { context.resources.getString(it) }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner = dialog.pin_channel_spinner.apply {
            this.adapter = spinnerAdapter
        }
        setSelected(title)
        dialog.titleText.setText("Add site to:")
        dialog.window?.setDimAmount(0.90f)
        dialog.confirm_action.setOnClickListener {
            val channel = channelTitles[spinner.selectedItemPosition]
            val url = sessionRepo.state.blockingFirst().currentUrl
            val pinTile = CustomPinnedTile(
                    id = UUID.randomUUID(),
                    url = url,
                    title = url)
            when (channel) {
                R.string.music_channel_title -> channelContentRepo.addToMusicChannel(pinTile)
                R.string.sports_channel_title -> channelContentRepo.addToSportsChannel(pinTile)
                R.string.news_channel_title -> channelContentRepo.addToNewsChannel(pinTile)
                else -> pinnedTileRepo.addPinnedTile(url,sessionRepo.currentURLScreenshot())
            }
            dialog.dismiss()
        }

        dialog.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun setSelected(title: Int) {
        var index = channelTitles.indexOf(title)
        spinner.setSelection(index)
    }

}