/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.tv.firefox.channels.content

import io.reactivex.subjects.BehaviorSubject
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.ChannelTile
import org.mozilla.tv.firefox.channels.TileSource
import org.mozilla.tv.firefox.channels.content.ChannelContent.setImage
import org.mozilla.tv.firefox.channels.pinnedtile.CustomPinnedTile

val _customSportsTiles: BehaviorSubject<MutableList<CustomPinnedTile>> = BehaviorSubject.create()

private val sportsTiles: MutableList<ChannelTile> = mutableListOf(
        ChannelTile(
                url = "https://www.nbcsports.com/video",
                title = "NBC Sports",
                subtitle = null,
                setImage = setImage(R.drawable.tile_sports_nbc_sports),
                tileSource = TileSource.SPORTS,
                id = "nbcSports"
        ),
        ChannelTile(
                url = "https://www.formula1.com/en/video.html",
                title = "Formula 1",
                subtitle = null,
                setImage = setImage(R.drawable.tile_sports_formula_1),
                tileSource = TileSource.SPORTS,
                id = "formula1"
        )
)

fun ChannelContent.getDefaultSportsTiles(): List<ChannelTile> = sportsTiles

fun ChannelContent.addSportsTile(tile: CustomPinnedTile) {
    _customSportsTiles.value?.add(0, tile)
}

fun ChannelContent.refreshSportsTiles(tiles: MutableList<CustomPinnedTile>) {
    _customSportsTiles.onNext(tiles)
}

fun ChannelContent.getCustomSportsTiles(): MutableList<CustomPinnedTile> = _customSportsTiles.value!!
