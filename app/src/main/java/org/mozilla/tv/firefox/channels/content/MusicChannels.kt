/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.tv.firefox.channels.content

import androidx.annotation.UiThread
import io.reactivex.subjects.BehaviorSubject
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.ChannelTile
import org.mozilla.tv.firefox.channels.TileSource


var musicTiles: MutableList<ChannelTile> = mutableListOf(
        ChannelTile(
                url = "https://www.npr.org/stations/",
                title = "NPR",
                subtitle = null,
                setImage = ChannelContent.setImage(R.drawable.tile_music_npr),
                tileSource = TileSource.MUSIC,
                id = "nprStations"
        ),
        ChannelTile(
                url = "https://bandcamp.com/#discover",
                title = "Bandcamp",
                subtitle = null,
                setImage = ChannelContent.setImage(R.drawable.tile_music_bandcamp),
                tileSource = TileSource.MUSIC,
                id = "bandcamp"
        )
)

val _musicChannels: BehaviorSubject<List<ChannelTile>> = BehaviorSubject.create()


fun ChannelContent.getMusicTiles() : List<ChannelTile> = musicTiles


fun ChannelContent.addToMusicChannel(channelTile: ChannelTile) {
    musicTiles.add(0,channelTile)
    refreshMusicTiles()
}

fun ChannelContent.refreshMusicTiles() {
    _musicChannels.onNext(ChannelContent.getMusicTiles())

}
