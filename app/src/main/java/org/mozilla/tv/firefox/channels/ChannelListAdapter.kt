package org.mozilla.tv.firefox.channels

import android.app.Dialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.dialog_pin_site.*
import kotlinx.android.synthetic.main.dialog_pin_site.cancelButton
import org.mozilla.tv.firefox.R

class ChannelListAdapter {

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

    fun onViewCreated(context: Context) {
        this.context = context
        dialog = Dialog(context, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_pin_site)
        spinnerAdapter =  ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, channelTitles.map { context.resources.getString(it) }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner = dialog.pin_channel_spinner.apply {
            this.adapter = spinnerAdapter
        }
        dialog.titleText.setText("Add site to:")
        dialog.window?.setDimAmount(0.85f)
        dialog.confirm_action.setOnClickListener {
            dialog.dismiss()
        }

        dialog.cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showDialog(title: Int = 0) {
        setSelected(title)
        dialog.show()
    }

    fun setSelected(title: Int) {
        var index = channelTitles.indexOf(title)
        spinner.setSelection(index)
    }

}