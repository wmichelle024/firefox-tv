package org.mozilla.tv.firefox.channels.content

import android.content.Context
import org.json.JSONArray
import org.mozilla.tv.firefox.R
import org.mozilla.tv.firefox.channels.pinnedtile.CustomPinnedTile

private const val PREF_HOME_TILES = "homeTiles"

class ChannelContentRepo(
        private val applicationContext: Context)
{
    private val CUSTOM_MUSIC_TILES = "customMusicTiles"
    private val CUSTOM_NEWS_TILES = "customNewsTiles"
    private val CUSTOM_SPORTS_TILES = "customSportsTiles"

    private val _sharedPreferences = applicationContext.getSharedPreferences(PREF_HOME_TILES, Context.MODE_PRIVATE)

    init {
        ChannelContent.refreshMusicTiles(loadCustomTiles(R.string.music_channel_title))
    }

    fun persistCustomToChannel(channelId: Int, tiles: MutableList<CustomPinnedTile>) {
        val channel : String = when (channelId) {
            R.string.music_channel_title -> CUSTOM_MUSIC_TILES
            R.string.sports_channel_title -> CUSTOM_SPORTS_TILES
            R.string.news_channel_title -> CUSTOM_NEWS_TILES
            else -> "Pinned (TODO)"
        }
        val tilesJSONArray = JSONArray()
        tiles.forEach { tilesJSONArray.put(it.toJSONObject()) }
        _sharedPreferences.edit().putString(channel, tilesJSONArray.toString()).apply()
    }

    fun loadCustomTiles(channelId: Int): MutableList<CustomPinnedTile> {
        val channel : String = when (channelId) {
            R.string.music_channel_title -> CUSTOM_MUSIC_TILES
            R.string.sports_channel_title -> CUSTOM_SPORTS_TILES
            R.string.news_channel_title -> CUSTOM_NEWS_TILES
            else -> "Pinned (TODO)"
        }
        val tilesJSONArray = JSONArray(_sharedPreferences.getString(channel, "[]"))
        val lhm = mutableListOf<CustomPinnedTile>()
        for (i in 0 until tilesJSONArray.length()) {
            val tileJSON = tilesJSONArray.getJSONObject(i)
            val tile = CustomPinnedTile.fromJSONObject(tileJSON)
            lhm.add(tile)
        }
        return lhm
    }

    fun addToMusicChannel(channelTile: CustomPinnedTile) {
        ChannelContent.addMusicTile(channelTile)
        persistCustomToChannel(R.string.music_channel_title, ChannelContent.getCustomMusicTiles())
        ChannelContent.refreshMusicTiles(loadCustomTiles(R.string.music_channel_title))
    }
}