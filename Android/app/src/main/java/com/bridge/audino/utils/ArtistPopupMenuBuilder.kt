package com.bridge.audino.utils

import android.content.Context
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.bridge.audino.model.Track

class ArtistPopupMenuBuilder {

    private lateinit var popupMenu: PopupMenu

    fun build(context: Context, view: View): ArtistPopupMenuBuilder {
        popupMenu = PopupMenu(context, view)
        return this
    }

    fun inflate(@MenuRes menuRes: Int): ArtistPopupMenuBuilder {
        popupMenu.menuInflater.inflate(menuRes, popupMenu.menu)
        return this
    }

    fun addAll(track: Track): ArtistPopupMenuBuilder {
        track.artists.forEachIndexed { index, artist ->
            add(index, artist.name)
        }
        return this
    }

    private fun add(itemId: Int, itemTitle: String): ArtistPopupMenuBuilder {
        popupMenu.menu.add(0, itemId, popupMenu.menu.size(), itemTitle)
        return this
    }

    fun setOnMenuItemClickListener(function: (Int) -> Unit): ArtistPopupMenuBuilder {
        popupMenu.setOnMenuItemClickListener { item ->
            function(item.itemId)
            true
        }
        return this
    }

    fun show() {
        popupMenu.show()
    }
}