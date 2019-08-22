/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.tv.firefox.channels.content

import io.reactivex.subjects.BehaviorSubject
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.ChannelTile
import org.mozilla.tv.firefox.channels.TileSource
import org.mozilla.tv.firefox.channels.pinnedtile.CustomPinnedTile

val _customMusicTiles: BehaviorSubject<MutableList<CustomPinnedTile>> = BehaviorSubject.create()

private var musicTiles: MutableList<ChannelTile> = mutableListOf(
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

fun ChannelContent.getDefaultMusicTiles(): List<ChannelTile> = musicTiles

fun ChannelContent.refreshMusicTiles(tiles: MutableList<CustomPinnedTile>) {
        _customMusicTiles.onNext(tiles)
}

fun ChannelContent.addMusicTile(tile: CustomPinnedTile) {
        _customMusicTiles.value?.add(tile)
}

fun ChannelContent.getCustomMusicTiles(): MutableList<CustomPinnedTile> = _customMusicTiles.value!!